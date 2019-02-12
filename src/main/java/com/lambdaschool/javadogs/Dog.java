package com.lambdaschool.javadogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Dog
{
    private @Id @GeneratedValue Long id;
    private String name;
    private int weight;
    private boolean apartment;

    public Dog()
    {

    }

    public Dog(String name, int weight, boolean apartment)
    {
        this.name = name;
        this.weight = weight;
        this.apartment = apartment;
    }
}
