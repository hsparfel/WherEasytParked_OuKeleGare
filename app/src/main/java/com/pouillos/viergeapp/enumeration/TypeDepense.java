package com.pouillos.viergeapp.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TypeDepense {

    //Objets directement construits
    Default(0,"?"),
    Fixe(1,"Fixe"),
    Courante(2,"Courante"),
    Occasionnelle(3,"Occasionnelle");

    public long id;
    public String name = "";

    //Constructeur
    TypeDepense(long id, String name){
        this.id = id;
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public List<TypeDepense> listAll() {
        List<TypeDepense> listToReturn = new ArrayList<>();
        List<TypeDepense> listTypeDepense = Arrays.asList(TypeDepense.values());
        for (TypeDepense typeDepense : listTypeDepense) {
            if (typeDepense != TypeDepense.Default) {
                listToReturn.add(typeDepense);
            }
        }
        return listToReturn;
    }
}
