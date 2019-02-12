package com.lambdaschool.javadogs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long>
{
    List<Dog> findAllByOrderByNameAsc();
    List<Dog> findAllByOrderByWeightAsc();
    Dog findByNameIgnoreCase(String breed);
    List<Dog> findByApartmentEquals(Boolean bool);
}