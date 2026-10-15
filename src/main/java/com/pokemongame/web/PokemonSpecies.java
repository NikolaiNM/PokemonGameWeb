package com.pokemongame.web;

import static com.pokemongame.web.utils.GameUtils.RANDOM;


public enum PokemonSpecies {
    PIKACHU("Пикачу", 2),
    BULBASAUR("Бульбазавр", 1),
    CHARMANDER("Чармандер", 2),
    SQUIRTLE("Сквиртл", 2),
    EEVEE("Иви", 1),
    JIGGLYPUFF("Джиглипафф", 1),
    MEOWTH("Мяут", 1),
    PSYDUCK("Псайдак", 2),
    MAGIKARP("Мэджикарп", 1),
    GYARADOS("Гьярадос", 3),
    SNORLAX("Снорлакс", 3),
    DRATINI("Дратини", 2),
    DRAGONITE("Драгонайт", 3),
    MACHOP("Мачоп", 2),
    GEODUDE("Джеодуд", 2),
    GASTLY("Гастли", 2),
    HAUNTER("Хонтер", 3),
    GENGAR("Генгар", 3),
    ONIX("Оникс", 2),
    RATTATA("Раттата", 1),
    PIDGEY("Пиджи", 1),
    SPEAROW("Спироу", 1),
    EKANS("Иканс", 2),
    SANDSHREW("Сэндшру", 2),
    NIDORAN("Нидоран", 2),
    VULPIX("Вульпикс", 2),
    ODDISH("Оддиш", 1),
    PARAS("Парас", 1),
    VENONAT("Венонат", 2),
    DIGLETT("Диглетт", 2);

    private final String russianName;
    private final int baseAttack;


    PokemonSpecies(String russianName, int baseAttack) {
        this.russianName = russianName;
        this.baseAttack = baseAttack;
    }

    public String getRussianName() {
        return russianName;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public static PokemonSpecies getRandom() {
        PokemonSpecies[] species = values();
        return species[RANDOM.nextInt(species.length)];
    }

}
