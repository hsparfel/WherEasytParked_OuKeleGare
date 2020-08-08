package com.pouillos.viergeapp.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FrequenceDepense {

    //Objets directement construits
    Default(0,"?", "?"),
    Ponctuel(1,"Ponctuel","Une fois"),
    Mensuel(4,"Mensuel","durant 1 mois"),
    Bimestriel(5,"Bimestriel","durant 2 mois"),
    Trimestriel(6,"Trimestriel","durant 3 mois"),
    Quadrimestriel(7,"Quadrimestriel","durant 4 mois"),
    Semestriel(8,"Semestriel","durant 6 mois"),
    Annuel(9,"Annuel","durant 12 mois");

    public long id;
    public String name = "";
    public String detail = "";

    //Constructeur
    FrequenceDepense(long id, String name, String detail){
        this.id = id;
        this.name = name;
        this.detail = detail;
    }

    public String toString(){
        return name;
    }

    public List<FrequenceDepense> listAll() {
        List<FrequenceDepense> listToReturn = new ArrayList<>();
        List<FrequenceDepense> listFrequenceDepense = Arrays.asList(FrequenceDepense.values());
        for (FrequenceDepense frequenceDepense : listFrequenceDepense) {
            if (frequenceDepense != FrequenceDepense.Default) {
                listToReturn.add(frequenceDepense);
            }
        }
        return listToReturn;
    }
}
