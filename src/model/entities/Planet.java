package model.entities;

import java.io.Serializable;

public class Planet implements Serializable{
    private Integer id;
    private String name;
    private String type;
    private Double diameter;
    private Double mass;
    private Double gravity;
    private Double orbitalSpeed;
    private Star star;
    
    public Planet(){}

    public Planet(Integer id, String name, String type, Double diameter, Double mass, Double gravity,
            Double orbitalSpeed, Star star) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.diameter = diameter;
        this.mass = mass;
        this.gravity = gravity;
        this.orbitalSpeed = orbitalSpeed;
        this.star = star;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public Double getOrbitalSpeed() {
        return orbitalSpeed;
    }

    public void setOrbitalSpeed(Double orbitalSpeed) {
        this.orbitalSpeed = orbitalSpeed;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Planet other = (Planet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
