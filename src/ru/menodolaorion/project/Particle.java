package ru.menodolaorion.project;

public class Particle {
    public String name;
    public int id;
    public Vector coordinates;
    public double potentialEnergy;

    public Particle(int id, String name, Vector vector) {
        this.name = name;
        this.id = id;
        coordinates = vector;
    }

    public Particle sum(Vector vector) {
        return new Particle(this.id, this.name, Vector.sum(this.coordinates, vector));
    }

    public boolean isEntered(Vector vector, double r0, double halfField) {
        double distance = coordinates.getDoubleDistance(vector, halfField);
        return (distance <= r0);
    }

    public double calculatePotentialEnergy(Particle[] particles, double r0_2, double rC_2, double halfField) {
        double pEnergy = 0;
        for (Particle particle : particles) {
            if (particle.id == id) { continue; }

            double radius = getRadius(particle.coordinates, r0_2, rC_2, halfField);
            if (radius == 0) {
                continue;
            }
            double radius_6 = Math.pow(radius, 3);
            double radius_12 = radius_6 * radius_6;
            pEnergy += 4 * (1 / radius_12 - 1 / radius_6);
        }

        return pEnergy;
    }

    private double getRadius(Vector vector, double r0_2, double rC_2, double halfField) {
        double distance = vector.getDoubleDistance(coordinates, halfField);
        return (distance > rC_2) ? 0 : (distance < r0_2) ? r0_2 : distance;
    }

    @Override
    public String toString() {
        return name + "(" +
                id + "): " +
                "coordinates = " + coordinates +
                ", potentialEnergy = " + potentialEnergy;
    }
}
