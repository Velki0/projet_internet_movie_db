package fr.diginamic;

import java.util.List;

public class Test {

    public static void main(String[] args) {

        String lieuBrut = "Santa Monica, California, USA ";
        List<String> lieuBruts = List.of(lieuBrut.split(","));
        for(String lieuBrut1 : lieuBruts){
            System.out.println(lieuBrut1);
        }

    }

}
