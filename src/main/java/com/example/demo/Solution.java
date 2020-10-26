package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Integer> listOriginal = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listOriginal.add(scanner.nextInt());
        }

        ArrayList<Integer> listDividido3 = new ArrayList<>();
        ArrayList<Integer> listDividido2 = new ArrayList<>();
        ArrayList<Integer> listSupplementary = new ArrayList<>();

        for (int i = 0; i < listOriginal.size(); i++) {
            boolean isDivisible = false;

            if (listOriginal.get(i) % 2 == 0) {
                listDividido2.add(listOriginal.get(i));
                isDivisible = true;
            }

            if (listOriginal.get(i) % 3 == 0) {
                listDividido3.add(listOriginal.get(i));
                isDivisible = true;
            }

            if (!isDivisible) {
                listSupplementary.add(listOriginal.get(i));
            }
        }

        printList(listDividido2);
        printList(listDividido3);
        printList(listSupplementary);
    }

    public static void printList(List<Integer> list) {
        for (Integer element : list) {
            System.out.println(element);
        }
    }
}