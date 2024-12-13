package MiniJava.parser;

import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.token.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohammad hosein on 6/25/2015.
 */

public class ParseTable {
    private final ArrayList<Map<Token, Action>> actionTable = new ArrayList<>();
    private final ArrayList<Map<NonTerminal, Integer>> gotoTable = new ArrayList<>();

    public ParseTable(String jsonTable) throws Exception {
        jsonTable = jsonTable.substring(2, jsonTable.length() - 2);

//      init Rows
        String[] Rows = jsonTable.split("],\\[");
        for (int i = 0; i < Rows.length; i++)
            Rows[i] = Rows[i].substring(1, Rows[i].length() - 1);

//      init cols
        String[] cols = Rows[0].split("\",\"");

//      init terminals
        Map<Integer, Token> terminals = new HashMap<>();
        for (int i = 1; i < cols.length; i++) {
            if (!cols[i].startsWith("Goto")) {
                terminals.put(i, new Token(Token.getTyepFormString(cols[i]), cols[i]));
            }
        }

//      init nonTerminals
        Map<Integer, NonTerminal> nonTerminals = new HashMap<>();
        for (int i = 1; i < cols.length; i++) {
            if (cols[i].startsWith("Goto")) {
                String temp = cols[i].substring(5);
                try {
                    nonTerminals.put(i, NonTerminal.valueOf(temp));
                } catch (Exception e) {
                    ErrorHandler.printError(e.getMessage());
                }
            }
        }

        //init action table
        for (int i = 1; i < Rows.length; i++) {
            String[] columns = Rows[i].split("\",\"");
            actionTable.add(new HashMap<>());
            for (int j = 1; j < columns.length; j++) {
                if (!columns[j].isEmpty()) {
                    if (columns[j].equals("acc")) {
                        actionTable.get(actionTable.size() - 1).put(terminals.get(j), new Action(act.accept, 0));
                    } else if (terminals.containsKey(j)) {
                        Token t = terminals.get(j);
                        Action a = new Action(columns[j].charAt(0) == 'r' ? act.reduce : act.shift, Integer.parseInt(columns[j].substring(1)));
                        actionTable.get(actionTable.size() - 1).put(t, a);
                    } else if (!nonTerminals.containsKey(j)) {
                        throw new Exception();
                    }
                }
            }
        }

        // init goto table
        for (int i = 1; i < Rows.length; i++) {
            String[] columns = Rows[i].split("\",\"");

            gotoTable.add(new HashMap<>());
            for (int j = 1; j < columns.length; j++) {
                if (!columns[j].isEmpty()) {
                    if (!columns[j].equals("acc") && !terminals.containsKey(j) && nonTerminals.containsKey(j)) {
                        gotoTable.get(gotoTable.size() - 1).put(nonTerminals.get(j), Integer.parseInt(columns[j]));
                    } else {
                        throw new Exception();
                    }
                }
            }
        }
    }

    public int getGotoTable(int currentState, NonTerminal variable) {
        return gotoTable.get(currentState).get(variable);
    }

    public Action getActionTable(int currentState, Token terminal) {
        return actionTable.get(currentState).get(terminal);
    }
}
