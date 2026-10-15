package com.pokemongame.web;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
    private static final int MAX_ACTIVE_POKEMONS = 5;
    private static final int MAX_LEVEL = 5;

    private final String name;
    private int level;
    private List<Pokemon> pokemons;
    private int totalWins = 0;


    public Trainer(String name) {
        this.name = name;
        this.level = 1;
        this.pokemons = new ArrayList<>(5);
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalWins() {
        return this.totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public Pokemon getPokemon(int index) {
        return this.pokemons.get(index);
    }

    public List<Pokemon> getPokemons() {
        return this.pokemons;
    }

    public Pokemon[] getActivePokemons() {
        List<Pokemon> activePokemons = new ArrayList<>();

        for (Pokemon pokemon : this.pokemons) {
            if (pokemon.isActive()) {
                activePokemons.add(pokemon);
            }
        }

        return activePokemons.toArray(new Pokemon[0]);
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.name)
                .append(" (Уровень: ").append(this.level)
                .append(", всего побед: ").append(this.totalWins).append(")\n");
        Pokemon[] activePokemons = this.getActivePokemons();
        if (activePokemons.length == 0) {
            sb.append("У вас нет активных покемонов\n");
        } else {
            sb.append("Покемоны:\n");
            for (Pokemon activePokemon : activePokemons) {
                sb.append(" - ").append(activePokemon.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    public Pokemon findActivePokemonByName(String pokemonName) {
        for (Pokemon pokemon : this.pokemons) {
            if (pokemon.getName().equalsIgnoreCase(pokemonName) && pokemon.isActive()) {
                return pokemon;
            }
        }
        return null;
    }

    public boolean addPokemon(Pokemon pokemon) {
        int activePokemonCount = 0;
        for (Pokemon p : pokemons) {
            if (p.isActive()) {
                activePokemonCount++;
            }
        }

        if (activePokemonCount >= MAX_ACTIVE_POKEMONS) {
            System.out.println("Нельзя добавить больше " + MAX_ACTIVE_POKEMONS + " активных покемонов!");
            return false;
        }

        pokemons.add(pokemon);
        return true;
    }

    public void addWin() {
        totalWins++;

        if (level < MAX_LEVEL) {
            if (totalWins % 3 == 0) {
                this.level++;
                if (this.level >= MAX_LEVEL) {
                    System.out.println("Поздравляем достигли максимального уровня и прошли игру");
                    System.out.println("Для выхода введите 'exit'");
                }
            }
        }
    }

    @Override
    public String toString() {
        return name + " (уровень: " + level + ", всего побед: " + totalWins + ")\nПокемоны: " + pokemons;
    }
}
