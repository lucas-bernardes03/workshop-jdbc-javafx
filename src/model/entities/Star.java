package model.entities;

import java.io.Serializable;

public class Star implements Serializable{
    private Integer id;
    private String name;
    private String stellarClass;
    private Double mass;
    
    public Star(){}

    public Star(Integer id, String name, String stellarClass, Double mass) {
        this.id = id;
        this.name = name;
        this.stellarClass = stellarClass;
        this.mass = mass;
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

    public String getStellarClass() {
        return stellarClass;
    }

    public void setStellarClass(String stellarClass) {
        this.stellarClass = stellarClass;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
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
        Star other = (Star) obj;
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
