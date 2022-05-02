package ru.menodolaorion.project;

public class SystemOfParticles {
    public final String NAME;
    public Particle[] particles;
    public Field field;

    public SystemOfParticles(int N, final String name, Field field) {
        NAME = name;
        particles = new Particle[N];
        this.field = field;
    }

    public void particleGenerate(double r0) {
        int countPartInLine = (int) Math.ceil(Math.pow(particles.length, 1.0/3));
        double freePlace = (field.width - countPartInLine * r0) / countPartInLine;
        int counter = 0;
        breakPoint:
        for (int i = 0; i < countPartInLine; i++) {
            for (int j = 0; j < countPartInLine; j++) {
                for (int k = 0; k < countPartInLine; k++) {
                    if (counter == particles.length) {
                        break breakPoint;
                    }
                    Vector vector = new Vector(
                            r0 / 2 + (r0 + freePlace) * k + RandomUtils.getRandom(0, freePlace),
                            r0 / 2 + (r0 + freePlace) * j + RandomUtils.getRandom(0, freePlace),
                            r0 / 2 + (r0 + freePlace) * i + RandomUtils.getRandom(0, freePlace));
                    particles[counter] = new Particle(++counter, NAME, vector);
                }
            }
        }
    }
    public void particleGenerate(double r0, boolean isRandom) {
        if (!isRandom) {
            particleGenerate(r0);
            return;
        }

        remake:
        for (int i = 0; i < particles.length; i++) {
            Vector vector = new Vector(RandomUtils.getRandom(0, field.width), RandomUtils.getRandom(0, field.width), RandomUtils.getRandom(0, field.width));
            System.out.println(i);
            for (int j = 0; j < i; j++) {
                if (particles[j].isEntered(vector, r0, field.width / 2)) {
                    i--;
                    continue remake;
                }
            }
            particles[i] = new Particle(i + 1, NAME, vector);
        }
    }

    public double calculateAllPotentialEnergy(double r0, double rC) {
        double pEnergy = 0;
        for (Particle particle : particles) {
            double dPE = particle.calculatePotentialEnergy(particles, r0 * r0, rC * rC, field.width / 2);
            particle.potentialEnergy = dPE;
            pEnergy += dPE;
        }
        return pEnergy;
    }
}
