package me.merunko.holocraft.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        List<String> completions = new ArrayList<>();


        if (args.length == 1) {

            completions.add("reload");
            completions.add("open");
            completions.add("mmoinspect");
            completions.add("inspect");
            completions.add("updatelboard");
            completions.add("calculate");
            completions.add("set");
            completions.add("debug");


        } else if (args.length == 2) {

            String subcommand = args[0].toLowerCase();
            switch (subcommand) {
                case "open":
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        completions.add(onlinePlayer.getName());
                    }
                    break;
                case "set":
                    completions.add("reward");
                    break;
                case "calculate":
                    completions.add("topitem");
                    break;
                case "debug":
                    completions.add("get");
                    completions.add("test");
                    break;
            }


        } else if (args.length == 3 && args[0].equalsIgnoreCase("debug")) {
            if (args[1].equalsIgnoreCase("get")) {
                completions.add("reward");
            } else if (args[1].equalsIgnoreCase("test")) {
                completions.add("announcer");
            }


        } else if (args.length == 4 && args[0].equalsIgnoreCase("debug")) {
            if (args[2].equalsIgnoreCase("announcer")) {
                completions.add("global");
                completions.add("personal");
            }


        } else if (args.length == 4 && args[0].equalsIgnoreCase("debug") && args[2].equalsIgnoreCase("reward")) {

            completions.add("top1");
            completions.add("top2");
            completions.add("top3");
            completions.add("top4");
            completions.add("top5");
            completions.add("top6");
            completions.add("top7");


        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {

            completions.add("top1");
            completions.add("top2");
            completions.add("top3");
            completions.add("top4");
            completions.add("top5");
            completions.add("top6");
            completions.add("top7");

        }

        String input = args[args.length - 1].toLowerCase();
        List<String> filteredCompletions = new ArrayList<>();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(input)) {
                filteredCompletions.add(completion);
            }
        }

        return filteredCompletions;
    }
}
