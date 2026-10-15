package com.pokemongame.web;

import static com.pokemongame.web.utils.GameUtils.RANDOM;


public class Battle {

    public static boolean fight(Pokemon playerPokemon, Pokemon enemyPokemon) {
        System.out.println("Battle");

        int playerLuck = RANDOM.nextInt(100) + 1;
        int enemyLuck = RANDOM.nextInt(100) + 1;

        double playerPower = playerLuck * playerPokemon.getLevel() * 0.1 * playerPokemon.getAttack();
        double enemyPower = enemyLuck * enemyPokemon.getLevel() * 0.1 * enemyPokemon.getAttack();

        if (playerPower >= enemyPower) {
            System.out.println("win");
            return true;
        } else {
            System.out.println("lose");
            return false;
        }
    }
}
