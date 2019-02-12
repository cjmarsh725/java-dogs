package com.lambdaschool.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        Dog foundDog = dogrepo.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        return assembler.toResource(foundDog);
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

    @PutMapping("/dogs/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable(value = "id") Long id,
                                                   @Valid @RequestBody Dog dogDetails)
    {
        Dog dog = dogrepo.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));
        dog.setName(dogDetails.getName());
        dog.setWeight(dogDetails.getWeight());
        dog.setApartment(dogDetails.isApartment());
        final Dog updatedDog = dogrepo.save(dog);
        return ResponseEntity.ok(updatedDog);
    }

    @PostMapping("/dogs")
    public Dog createDog(@Valid @RequestBody Dog dog) {
        return dogrepo.save(dog);
    }

    @DeleteMapping("/dogs/{id}")
    public String updateDog(@PathVariable(value = "id") Long id)
    {
        Dog dog = dogrepo.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));
        dogrepo.delete(dog);
        return "Dog deleted";
    }
}
