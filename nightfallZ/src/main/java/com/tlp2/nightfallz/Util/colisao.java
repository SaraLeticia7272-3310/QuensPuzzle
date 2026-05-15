package com.tlp2.nightfallz.Util;

public class colisao {

    public static boolean verificar(
            double x1, double y1, double largura1, double altura1,
            double x2, double y2, double largura2, double altura2
    ) {

        return x1 < x2 + largura2 &&
               x1 + largura1 > x2 &&
               y1 < y2 + altura2 &&
               y1 + altura1 > y2;
    }
}