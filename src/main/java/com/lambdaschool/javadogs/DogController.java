package com.lambdaschool.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class DogController
{
    private final DogRepository dogrepo;
    private final DogResourceAssembler assembler;

    public DogController(DogRepository dogrepo, DogResourceAssembler assembler)
    {
        this.dogrepo = dogrepo;
        this.assembler = assembler;
    }

    // ************ TEMP ************
    @GetMapping("/dogs")
    public Resources<Resource<Dog>> all()
    {
        List<Resource<Dog>> dogs = dogrepo.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/{id}")
    public Resource<Dog> findOne(@PathVariable Long id)
    {
        Dog foundEmp = dogrepo.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        return assembler.toResource(foundEmp);
    }
    // ************ TEMP ************

    @GetMapping("/dogs/breeds")
    public Resources<Resource<Dog>> sortByBreed()
    {
        List<Resource<Dog>> dogs = dogrepo.findAllByOrderByNameAsc().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> sortByWeight()
    {
        List<Resource<Dog>> dogs = dogrepo.findAllByOrderByWeightAsc().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/breeds/{breed}")
    public Resource<Dog> findOneBreed(@PathVariable String breed)
    {
        Dog foundDog = dogrepo.findByNameIgnoreCase(breed);
        return assembler.toResource(foundDog);
    }

    @GetMapping("/dogs/apartment")
    public Resources<Resource<Dog>> getIsApartment()
    {
        List<Resource<Dog>> dogs = dogrepo.findByApartmentEquals(true).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }
}
