package com.pokemongame.web;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


public class GameStateStorage {
    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE_NAME = "savegame.csv";

    /**
     * Сохраняет игру в CSV файл
     * Формат:
     * Строка 1: ИмяТренера,УровеньТренера,ВсегоПобед
     * Строка 2+: ИмяПокемона,Уровень,Атака,СчетчикПобед,Активен
     */
    public static void save(Trainer trainer) {
        File directory = new File(SAVE_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, SAVE_FILE_NAME);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(trainer.getName() + "," + trainer.getLevel() + "," + trainer.getTotalWins());

            for (Pokemon pokemon : trainer.getPokemons()) {
                writer.printf("%s,%d,%d,%d,%b%n",
                        pokemon.getName(),
                        pokemon.getLevel(),
                        pokemon.getAttack(),
                        pokemon.getCountWins(),
                        pokemon.isActive()
                );
            }
            System.out.println("Сохранено в " + file);

        } catch (Exception e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    /**
     * Загружает игру из CSV файла
     * @return объект Trainer или null если файл не найден
     */
    public static Trainer load() {
        File directory = new File(SAVE_DIR);
        File file = new File(directory, SAVE_FILE_NAME);

        if (!file.exists()) {
            System.out.println("Файл не найден");
            return null;
        }

        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                System.out.println("Нет данных");
                return null;
            }

            String[] trainerData = scanner.nextLine().split(",");

            Trainer loadedTrainer = new Trainer(trainerData[0]);
            loadedTrainer.setLevel(Integer.parseInt(trainerData[1]));
            loadedTrainer.setTotalWins(Integer.parseInt(trainerData[2]));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] pokemonData = line.split(",");

                if (pokemonData.length != 5) {
                    System.err.println("Проблемный покемон: " + line);
                    continue;
                }

                String name = pokemonData[0];
                int level = Integer.parseInt(pokemonData[1]);
                int attack = Integer.parseInt(pokemonData[2]);
                int countWins = Integer.parseInt(pokemonData[3]);
                boolean isActive = Boolean.parseBoolean(pokemonData[4]);

                Pokemon pokemon = new Pokemon(name, level, attack, countWins, isActive);

                loadedTrainer.addPokemon(pokemon);
            }

            System.out.println("Загруженно");

            return loadedTrainer;

        } catch (Exception e) {
            System.err.println("Ошибка при загрущке: " + e.getMessage());
            return null;
        }
    }
}
