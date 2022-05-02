package ru.menodolaorion.project;

import java.util.Arrays;

public class Vector {
    public double[] coordinate;

    public Vector(double X, double Y, double Z) {
        coordinate = new double[] {X, Y, Z};
    }

    public Vector(double... coordinate) {
        this.coordinate = (coordinate);
    }

    public void sum(Vector vector) {
        for (int i = 0; i < coordinate.length; i++) {
            coordinate[i] += vector.coordinate[i];
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(coordinate);
    }

    public static Vector sum(Vector firstVector, Vector secondVector) {
        double[] coordinate = new double[3];
        for (int i = 0; i < coordinate.length; i++) {
            coordinate[i] = secondVector.coordinate[i] + firstVector.coordinate[i];
        }
        return new Vector(coordinate);
    }

    public static Vector getInverted(Vector vector) {
        return new Vector(-vector.coordinate[0], -vector.coordinate[1], -vector.coordinate[2]);
    }

    public double getDoubleDistance(Vector vector, double halfField) {
        Vector v = sum(this, Vector.getInverted(vector));
        double distance = 0;
        for (double coord : v.coordinate) {
            double radius = (Math.abs(coord) > halfField) ? coord - Math.signum(coord) * halfField * 2 : coord;
            distance += radius * radius;
        }

        return distance;
    }
}
