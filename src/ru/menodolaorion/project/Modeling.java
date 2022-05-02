package ru.menodolaorion.project;

import java.util.ArrayList;

public class Modeling {
    private int countParticles;
    public double
            temperature, density, volume, volume_3,
            r0, rC,
            dR;
    private SystemOfParticles systemParticles;
    private int countPotentialEnergy = 0;
    private double currentPotentialEnergy = 0;
    private double sumPotentialEnergy = 0;
    private ArrayList<Double> potentialEnergy = new ArrayList<Double>();

    public Modeling(double T, double P, final int N, double r0, double rC, double dR) {
        temperature = T;
        density = P;
        countParticles = N;
        volume = countParticles / density;
        this.r0 = r0;
        this.rC = rC;
        this.dR = dR;
        systemParticles = new SystemOfParticles(countParticles, "Argon", fieldGenerate());
    }

    private Field fieldGenerate() {
        volume_3 = Math.pow(volume, 1.0/3);
        return new Field(volume_3);
    }

    private void particleGenerate(boolean isRandom) {
        systemParticles.particleGenerate(r0, isRandom);
    }

    private void calculateAllPotentialEnergy() {
        currentPotentialEnergy = systemParticles.calculateAllPotentialEnergy(r0, rC);
        potentialEnergy.add(currentPotentialEnergy);
    }

    private boolean isModeling = false;
    private int countSucceeded = 0;

    private void startModeling(int totalCount, int checkCount, boolean isCommented, int sleep) throws InterruptedException {
        isModeling = true;
        int counter = 0;
        while (isModeling) {
            Particle particle = systemParticles.particles[RandomUtils.getRandom(0, countParticles)];
            Vector dimension = new Vector(RandomUtils.getRandom(0, dR * volume_3), RandomUtils.getRandom(0, dR * volume_3), RandomUtils.getRandom(0, dR * volume_3));
            getMove(particle, dimension, isCommented);

            if (counter == totalCount) { isModeling = false; }
            if ((++counter % checkCount) == 0) {
                potentialEnergy.add(sumPotentialEnergy / checkCount);
                sumPotentialEnergy = 0;
                System.out.println("Потенциальная энергия: " + potentialEnergy.get(countPotentialEnergy++) + " (" + 100.0 * countSucceeded / checkCount + "%)");
                countSucceeded = 0;
                Thread.sleep(sleep);
            }
        }
    }
    private void getMove(Particle particle, Vector dimension, boolean isCommented) {
        if (isCommented) { System.out.println("Пробуем сдвинуть " + particle.name + "(" + particle.id + ") на " + dimension); }
        Particle sumParticle = particle.sum(dimension);
        double newEnergy = sumParticle.calculatePotentialEnergy(systemParticles.particles, r0 * r0, rC * rC, volume_3 / 2);
        double dEnergy = newEnergy - particle.potentialEnergy;
        if ((dEnergy > 0) && (RandomUtils.getRandom(0, 1.0) < Math.exp(-dEnergy / temperature))) {
            if (isCommented) { System.out.println("Попытка отменена."); }
            return;
        }
        if (isCommented) { System.out.println("Попытка успешна."); }
        countSucceeded++;
        particle.coordinates = sumParticle.coordinates;
        particle.potentialEnergy = newEnergy;
        currentPotentialEnergy += dEnergy;
        sumPotentialEnergy += currentPotentialEnergy;
    }

    public static void main(String[] args) throws InterruptedException {
        double
                T = 1,
                P = 1,
                r0 = 0.8,
                rC = 2.5,
                dR = 0.05;
        int N = 108;

        Modeling modeling = new Modeling(T, P, N, r0, rC, dR);
        modeling.particleGenerate(true);
        modeling.calculateAllPotentialEnergy();

        modeling.startModeling(1_250_000_000, 12_500_000, false, 0);
    }
}
