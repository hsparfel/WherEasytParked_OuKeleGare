package com.pouillos.viergeapp.entities;

import com.pouillos.viergeapp.enumeration.TypeDepense;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CategorieDepense implements Comparable<CategorieDepense> {

    @Id
    private Long id;

    @NotNull
    private String nom;

    @Convert(converter = TypeDepenseConverter.class, columnType = Long.class)
    private TypeDepense typeDepense;





    @Generated(hash = 339220072)
    public CategorieDepense(Long id, @NotNull String nom, TypeDepense typeDepense) {
        this.id = id;
        this.nom = nom;
        this.typeDepense = typeDepense;
    }





    @Generated(hash = 1083271960)
    public CategorieDepense() {
    }





    public Long getId() {
        return this.id;
    }





    public void setId(Long id) {
        this.id = id;
    }





    public String getNom() {
        return this.nom;
    }





    public void setNom(String nom) {
        this.nom = nom;
    }





    public TypeDepense getTypeDepense() {
        return this.typeDepense;
    }





    public void setTypeDepense(TypeDepense typeDepense) {
        this.typeDepense = typeDepense;
    }





    public static class TypeDepenseConverter implements PropertyConverter<TypeDepense, Long> {
        @Override
        public TypeDepense convertToEntityProperty(Long databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (TypeDepense typeDepense : TypeDepense.values()) {
                if (typeDepense.id == databaseValue) {
                    return typeDepense;
                }
            }
            return TypeDepense.Default;
        }

        @Override
        public Long convertToDatabaseValue(TypeDepense entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public int compareTo(CategorieDepense o) {
        return this.nom.compareTo(o.nom);
    }
}
