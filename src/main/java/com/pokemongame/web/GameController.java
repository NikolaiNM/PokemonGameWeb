package com.pokemongame.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class GameController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/new")
    public String createTrainer(@RequestParam String trainerName, HttpSession session) {
        Trainer trainer = new Trainer(trainerName);
        // Сохраняем тренера в сессии (памяти сервера)
        session.setAttribute("trainer", trainer);
        return "redirect:/game";
    }

    @GetMapping("/game")
    public String gamePage(HttpSession session, Model model) {
        // Достаем тренера из сессии
        Trainer trainer = (Trainer) session.getAttribute("trainer");

        if (trainer == null) {
            return "redirect:/"; // Если тренера нет — на главную
        }

        model.addAttribute("trainer", trainer);
        return "game";
    }

    @PostMapping("/add")
    public String addPokemon(@RequestParam String pokemonName, HttpSession session) {
        Trainer trainer = (Trainer) session.getAttribute("trainer");

        if (trainer != null) {
            Pokemon pokemon = new Pokemon(pokemonName);
            trainer.addPokemon(pokemon);
            // Обновляем тренера в сессии
            session.setAttribute("trainer", trainer);
        }

        return "redirect:/game";
    }

    @PostMapping("/search")
    public String searchWildPokemons(HttpSession session) {
        Trainer trainer = (Trainer) session.getAttribute("trainer");

        if (trainer != null) {
            // Генерируем 4 диких покемона
            Pokemon[] wildPokemons = new Pokemon[4];
            for (int i = 0; i < wildPokemons.length; i++) {
                PokemonSpecies species = PokemonSpecies.getRandom();
                wildPokemons[i] = new Pokemon(species.getRussianName(), species.getBaseAttack());
            }
            // Сохраняем диких покемонов в сессии
            session.setAttribute("wildPokemons", wildPokemons);
        }

        return "redirect:/battle";
    }

    @GetMapping("/battle")
    public String battlePage(HttpSession session, Model model) {
        Trainer trainer = (Trainer) session.getAttribute("trainer");
        Pokemon[] wildPokemons = (Pokemon[]) session.getAttribute("wildPokemons");

        if (trainer == null || wildPokemons == null) {
            return "redirect:/"; // Если нет тренера или диких покемонов — на главную
        }

        model.addAttribute("trainer", trainer);
        model.addAttribute("wildPokemons", wildPokemons);
        return "battle";
    }

    @PostMapping("/attack")
    public String attack(
            @RequestParam String myPokemonName,
            @RequestParam int enemyId,
            HttpSession session,
            RedirectAttributes redirectAttributes) { // <-- Нужен для передачи сообщения

        Trainer trainer = (Trainer) session.getAttribute("trainer");
        Pokemon[] wildPokemons = (Pokemon[]) session.getAttribute("wildPokemons");

        if (trainer != null && wildPokemons != null && enemyId >= 0 && enemyId < wildPokemons.length) {
            // Ищем покемона игрока
            Pokemon myPokemon = trainer.findActivePokemonByName(myPokemonName);
            Pokemon enemy = wildPokemons[enemyId];

            if (myPokemon != null) {
                // Запускаем битву (логика из старого проекта)
                boolean isWin = Battle.fight(myPokemon, enemy);

                String resultMessage;
                if (isWin) {
                    myPokemon.win();
                    trainer.addWin();
                    resultMessage = " Победа! " + myPokemon.getName() + " победил " + enemy.getName() + "!";
                } else {
                    myPokemon.lose();
                    resultMessage = "💀 Поражение... " + myPokemon.getName() + " отправляется в нокаут.";
                }

                // Передаем сообщение на следующую страницу
                redirectAttributes.addFlashAttribute("battleMessage", resultMessage);
            } else {
                redirectAttributes.addFlashAttribute("battleMessage", "❌ Покемон не найден или уже в нокауте!");
            }

            // Очищаем диких покемонов после битвы (как в консоли)
            session.removeAttribute("wildPokemons");
            // Сохраняем обновленного тренера
            session.setAttribute("trainer", trainer);

            if (trainer.getLevel() >= 5) {
                redirectAttributes.addFlashAttribute("victoryMessage", "🎉 ПОЗДРАВЛЯЕМ! Ты достиг 5 уровня и стал Мастером Покемонов! ");
            }
        }

        return "redirect:/game";
    }

    @PostMapping("/save")
    public String saveGame(HttpSession session, RedirectAttributes redirectAttributes) {
        Trainer trainer = (Trainer) session.getAttribute("trainer");

        if (trainer != null) {
            GameStateStorage.save(trainer);
            redirectAttributes.addFlashAttribute("gameMessage", "✅ Игра сохранена!");
        } else {
            redirectAttributes.addFlashAttribute("gameMessage", "❌ Нечего сохранять.");
        }

        return "redirect:/game";
    }

    @PostMapping("/load")
    public String loadGame(HttpSession session, RedirectAttributes redirectAttributes) {
        Trainer loadedTrainer = GameStateStorage.load();

        if (loadedTrainer != null) {
            session.setAttribute("trainer", loadedTrainer);
            session.removeAttribute("wildPokemons"); // Сбрасываем диких покемонов
            redirectAttributes.addFlashAttribute("gameMessage", "✅ Игра загружена! Привет, " + loadedTrainer.getName() + "!");
        } else {
            redirectAttributes.addFlashAttribute("gameMessage", "️ Файл сохранения не найден.");
        }

        return "redirect:/game";
    }

    @PostMapping("/reset")
    public String resetGame(HttpSession session) {
        session.invalidate(); // Полностью очищаем сессию
        return "redirect:/";
    }

    @PostMapping("/load-start")
    public String loadFromStart(HttpSession session, RedirectAttributes redirectAttributes) {
        // Пытаемся загрузить тренера из файла
        Trainer loadedTrainer = GameStateStorage.load();

        if (loadedTrainer != null) {
            // Если файл есть — кладем тренера в сессию и идем в игру
            session.setAttribute("trainer", loadedTrainer);
            return "redirect:/game";
        } else {
            // Если файла нет — возвращаем на главную с ошибкой
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ Файл сохранения не найден. Создайте нового тренера.");
            return "redirect:/";
        }
    }
}