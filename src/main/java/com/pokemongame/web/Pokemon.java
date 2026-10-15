package com.pokemongame.web;

import static com.pokemongame.web.utils.GameUtils.RANDOM;


public class Pokemon {
    private static final int WINS_TO_LEVEL_UP = 3;
    private static final int MAX_POKEMON_LEVEL = 5;
    private static final int WILD_POKEMONS = 4;

    private final String name;
    private int level;
    private int attack;
    private int countWins;
    private boolean isActive;
    private int totalWins = 0;

    public Pokemon(String name) {
        this.name = name;
        this.level = 1;
        this.attack = 1;
        this.countWins = 0;
        this.isActive = true;
    }

    public Pokemon(String name, int attack) {
        this.name = name;
        this.level = RANDOM.nextInt(2) + 1;
        this.attack = attack;
        this.countWins = 0;
        this.isActive = true;
    }

    public Pokemon(String name, int level, int attack, int countWins, boolean isActive) {
        this.name = name;
        this.level = level;
        this.attack = attack;
        this.countWins = countWins;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getCountWins() {
        return countWins;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public static Pokemon[] searchWild() {
        Pokemon[] wildPokemons = new Pokemon[WILD_POKEMONS];

        for (int i = 0; i < WILD_POKEMONS; i++) {
            PokemonSpecies wildPokemon = PokemonSpecies.getRandom();
            wildPokemons[i] = new Pokemon(wildPokemon.getRussianName(), wildPokemon.getBaseAttack());
        }
        return wildPokemons;
    }

    public void win() {
        this.countWins++;
        this.totalWins++;
        this.attack = Math.min(this.attack + 1, WINS_TO_LEVEL_UP);

        if (this.countWins >= WINS_TO_LEVEL_UP) {
            if (this.level < MAX_POKEMON_LEVEL) {
                this.level++;
                this.attack = 1;
                this.countWins = 0;
            } else {
                this.countWins = 0;
            }
        }
    }

    public void lose() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        if (isActive) {
            return name + " (уровень: " + level + ", атака: " + attack + ", всего побед: " + totalWins + ")";
        } else {
            return "";
        }
    }
}
