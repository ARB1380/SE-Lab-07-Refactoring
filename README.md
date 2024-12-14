# SE-Lab-07-Refactoring


## بازآرایی‌ها

### Facade #1
در متد Main کد زیر را داشتیم:


```java

Parser parser = new Parser();
try {
    // start parsing
    parser.startParse(new Scanner(new File("src/main/resources/code")));
} catch (FileNotFoundException e) {
    ErrorHandler.printError(e.getMessage());
}

```

این منطق کمی برای کاربری که شروع به استفاده از کتابخانه می‌کند، سخت است. پس بازآرایی زیر انجام شد که راه آسان استفاده از تابع پارس برای فایل است:


```java

ParserFacade.parseFile("src/main/resources/code");

```


### Facade #2
در متد Parser کد زیر را داشتیم:


```java

parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));

```

این منطق کمی برای کاربری که شروع به استفاده از کتابخانه می‌کند، سخت است. پس بازآرایی زیر انجام شد که راه آسان ساخت parse table با استفاده از فایل است:


```java

parseTable = ParseTableFacade.createParseTableFromFile("src/main/resources/parseTable");

```
### Strategy
سومین مورد بازآرایی خواسته شده از ما در این پروژه مربوط به این مورد بود که در کلاس code generator و تابع semantic function تعداد 33 مورد case وجود داشت که برای بازآرایی از این تکنیک استفاده شده است. در اینجا یک interface به نام action تعریف شده است و متد execute action در آن نهادینه شده است که در واقع همان تابع مربوطه در code generator صدا زده می شود. همچنین به ازای هرکدام از آن متدها یک class مجزا تعریف شده است که از این interface استفاده می کند و تابع مربوط به خودش را در execute action صدا می زند. در زیر می توان interface و همچنین یکی از کلاس های نمونه را مشاهده کرد. 
```java
public interface Action {
    void executeAction(CodeGenerator codeGenerator, Token next);
}
```
```java
public class AddAction implements Action {
    @Override
    public void executeAction(CodeGenerator codeGenerator, Token next) {
        codeGenerator.add();
    }
}
```
حال در خود کلاس code generator در کانستراکتور آن یک map از شماره به آبجکت مربوطه ایجاد شده است و همچنین در تابع semanticFunction با گرفتن عملکرد خواسته شده همان تابع execute action را صدا می زنیم. در شکل زیر می توانید کد های تغییر یافته را مشاهده کنید : 

```java
    public CodeGenerator() {
        symbolTable = new SymbolTable(getMemory());
        //TODO
        numberToAction = new HashMap<>();
        numberToAction.put(1, new CheckIdAction());
        numberToAction.put(2, new PidAction());
        numberToAction.put(3, new FPidAction());
        numberToAction.put(4, new KPidAction());
        numberToAction.put(5, new IntPidAction());
        numberToAction.put(6, new StartCallAction());
        numberToAction.put(7, new CallAction());
        numberToAction.put(8, new ArgAction());
        numberToAction.put(9, new AssignAction());
        numberToAction.put(10, new AddAction());
        numberToAction.put(11, new SubAction());
        numberToAction.put(12, new MultAction());
        numberToAction.put(13, new LabelAction());
        numberToAction.put(14, new SaveAction());
        numberToAction.put(15, new WhileAction());
        numberToAction.put(16, new JpfSaveAction());
        numberToAction.put(17, new JpHereAction());
        numberToAction.put(18, new PrintAction());
        numberToAction.put(19, new EqualAction());
        numberToAction.put(20, new LessThanAction());
        numberToAction.put(21, new AndAction());
        numberToAction.put(22, new NotAction());
        numberToAction.put(23, new DefClassAction());
        numberToAction.put(24, new DefMethodAction());
        numberToAction.put(25, new PopClassAction());
        numberToAction.put(26, new ExtendAction());
        numberToAction.put(27, new DefFieldAction());
        numberToAction.put(28, new DefVarAction());
        numberToAction.put(29, new MethodReturnAction());
        numberToAction.put(30, new DefParamAction());
        numberToAction.put(31, new LastTypeBoolAction());
        numberToAction.put(32, new LastTypeIntAction());
        numberToAction.put(33, new DefMainAction());
    }
```
```java
    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        Action action = numberToAction.get(func);
        if(action != null){
            action.executeAction(this, next);
        }
    }
```
### separate query from modifier
در متدهای زیر مقدار یک متغیر هم دارد تغییر می‌کند و هم باز گردانده می‌شود:
```java
public int getTemp() {
    lastTempIndex += tempSize;
    return lastTempIndex - tempSize;
}

public int getDateAddress() {
    lastDataAddress += dataSize;
    return lastDataAddress - dataSize;
}
```
هر بار صدا شدن این دو تابع را با توابع زیر جایگزین می‌کنیم:
```java
    public int getTemp() {
        return lastTempIndex;
    }

    public void updateTemp() {
        lastTempIndex += tempSize;
    }

    public int getDataAddress() {
        return lastDataAddress;
    }

    public void updateDataAddress() {
        lastDataAddress += dataSize;
    }
```

### Self-encapsulate field
در کلاس CodeGenerator تعدادی قیلد private وجود دارد که به طور مستقیم توسط متدهای این کلاس استفاده می‌شوند. می‌توانیم این فیلدها را encapsulate کنیم:
```java
    private Memory memory = new Memory();
    private Stack<Address> ss = new Stack<Address>();
    private Stack<String> symbolStack = new Stack<>();
    private Stack<String> callStack = new Stack<>();
    private SymbolTable symbolTable;
```
پس از افزودن متدهای getter داریم:
```java
private Memory memory = new Memory();
    private Stack<Address> ss = new Stack<Address>();
    private Stack<String> symbolStack = new Stack<>();
    private Stack<String> callStack = new Stack<>();
    private SymbolTable symbolTable;

    public Memory getMemory() {
        return memory;
    }

    public Stack<Address> getSs() {
        return ss;
    }

    public Stack<String> getSymbolStack() {
        return symbolStack;
    }

    public Stack<String> getCallStack() {
        return callStack;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
```

### Custom refactors
کلاس ParseTable را در نظر می‌گیریم که یک Constructor بسیار بزرگ دارد که از پیچیدگی زیادی برخوردر است.
```java
    private ArrayList<Map<Token, Action>> actionTable;

    private ArrayList<Map<NonTerminal, Integer>> gotoTable;

    public ParseTable(String jsonTable) throws Exception {
        jsonTable = jsonTable.substring(2, jsonTable.length() - 2);
        String[] Rows = jsonTable.split("\\],\\[");
        Map<Integer, Token> terminals = new HashMap<Integer, Token>();
        Map<Integer, NonTerminal> nonTerminals = new HashMap<Integer, NonTerminal>();
        Rows[0] = Rows[0].substring(1, Rows[0].length() - 1);
        String[] cols = Rows[0].split("\",\"");
        for (int i = 1; i < cols.length; i++) {
            if (cols[i].startsWith("Goto")) {
                String temp = cols[i].substring(5);
                try {
                    nonTerminals.put(i, NonTerminal.valueOf(temp));
                } catch (Exception e) {
                    ErrorHandler.printError(e.getMessage());
                }
            } else {
                terminals.put(i, new Token(Token.getTyepFormString(cols[i]), cols[i]));
            }
        }
        actionTable = new ArrayList<Map<Token, Action>>();
        gotoTable = new ArrayList<Map<NonTerminal, Integer>>();
        for (int i = 1; i < Rows.length; i++) {
            Rows[i] = Rows[i].substring(1, Rows[i].length() - 1);
            cols = Rows[i].split("\",\"");
            actionTable.add(new HashMap<Token, Action>());
            gotoTable.add(new HashMap<>());
            for (int j = 1; j < cols.length; j++) {
                if (!cols[j].equals("")) {
                    if (cols[j].equals("acc")) {
                        actionTable.get(actionTable.size() - 1).put(terminals.get(j), new Action(act.accept, 0));
                    } else if (terminals.containsKey(j)) {
                        Token t = terminals.get(j);
                        Action a = new Action(cols[j].charAt(0) == 'r' ? act.reduce : act.shift, Integer.parseInt(cols[j].substring(1)));
                        actionTable.get(actionTable.size() - 1).put(t, a);]
                    } else if (nonTerminals.containsKey(j)) {
                        gotoTable.get(gotoTable.size() - 1).put(nonTerminals.get(j), Integer.parseInt(cols[j]));
                    } else {
                        throw new Exception();
                    }
                }
            }
        }
    }
```
در ظی چند مرحله با چند ریفکتور این کانستراکتور را مرتب می‌کنیم.
۱- مراحل ایجاد هر کدام از متغیرهای لوکال constructor را جدا کنیم:
```java
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
```
در نهایت با extract function می‌توان خوانایی کد را بهبود بخشید:
```java
private final ArrayList<Map<Token, Action>> actionTable = new ArrayList<>();
    private final ArrayList<Map<NonTerminal, Integer>> gotoTable = new ArrayList<>();

    public ParseTable(String jsonTable) throws Exception {
        jsonTable = jsonTable.substring(2, jsonTable.length() - 2);
        
        String[] Rows = jsonTable.split("],\\[");
        for (int i = 0; i < Rows.length; i++)
            Rows[i] = Rows[i].substring(1, Rows[i].length() - 1);

        String[] cols = Rows[0].split("\",\"");
        
        Map<Integer, Token> terminals = initTerminals(cols);
        Map<Integer, NonTerminal> nonTerminals = initNonTerminals(cols);

        initActionTable(Rows, terminals, nonTerminals);
        initGotoTable(Rows, terminals, nonTerminals);
    }

    private static Map<Integer, Token> initTerminals(String[] cols) {
        Map<Integer, Token> terminals = new HashMap<>();
        for (int i = 1; i < cols.length; i++) {
            if (!cols[i].startsWith("Goto")) {
                terminals.put(i, new Token(Token.getTyepFormString(cols[i]), cols[i]));
            }
        }
        return terminals;
    }

    private Map<Integer, NonTerminal> initNonTerminals(String[] cols) {
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
        return nonTerminals;
    }

    private void initGotoTable(String[] Rows, Map<Integer, Token> terminals, Map<Integer, NonTerminal> nonTerminals) throws Exception {
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

    private void initActionTable(String[] Rows, Map<Integer, Token> terminals, Map<Integer, NonTerminal> nonTerminals) throws Exception {
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
    }
```
## سوالات


### سوال ۲
۱- دسته‌ی bloaters:
متدها یا کلاس‌هایی که بیش از اندازه بزرگ شده‌اند به طوری که فهم و تغییر آنها بسیار دشوار است.
۲- دسته‌ی Object orientation abusers:
استفاده‌های نادرست از مفاهیم شی گرایی در نوشتن برنامه به گونه‌ای که خوانایی و درک کد را سخت می‌کند.
۳- دسته‌ی change preventers:
کدی که به نحوی زده شده که جلوی تغییر دادن کد را بگیرد و تغییر را سحت کند. مثلا این که لازم باشد برای یک تغییر به ظاهر کوچک نقاط زیادی از کد را تغییر دهیم.
۴- دسته‌ی dispensables:
بخش‌هایی از کد که کارایی خوبی ندارند و می‌توانند بدون آسیب زدن به کدبیس حذف شوند و تنها کار توسعه را سخت می‌کنند.
۵- دسته‌ی couplers:
بوهایی که باعث کاپلینگ بالا و وابستگی زیادی اجزای کد به یکدیکر می‌شود.

### سوال ۴

برای مثال کد [Phase2CodeFileManipulator.java](https://github.com/bigsheykh/Convert_UML_to_ANSI_C/blob/master/src/com/project/phase2CodeGeneration/Phase2CodeFileManipulator.java) را در نظر می‌گیریم:

1. Bloaters

Long Method:

متد generatePhase2 بسیار طولانی است و مسئولیت‌های متعددی دارد، که باعث دشواری در درک و تست آن می‌شود. این متد می‌تواند به چند متد کوچکتر تقسیم شود که هر کدام یک بخش مشخص از مسئولیت را انجام دهند.
متدهای دیگر نظیر methodCallHandler و generatePhase2 نیز بزرگ هستند و نیاز به refactoring دارند.


Large Class:

کلاس Phase2CodeFileManipulator بسیار بزرگ است و وظایف مختلفی را انجام می‌دهد. این کلاس می‌تواند به چندین کلاس کوچک‌تر تقسیم شود که هر کدام یک بخش خاص از فرآیند مدیریت فایل‌های فاز دوم را انجام دهند.


Primitive Obsession:

استفاده از مقادیر ابتدایی مانند int برای مواردی نظیر depthOfParenthesis و depthOfCurlyBracket می‌تواند با کلاس‌های انتزاعی‌تر یا enumهای جدید جایگزین شود.


2. Object-Orientation Abusers

Switch Statements:

در متد generatePhase2 از یک بلوک بزرگ switch استفاده شده است که نشانه‌ای از نقض اصل شیءگرایی است. این کد می‌تواند با استفاده از الگوی استراتژی (Strategy Pattern) یا متدهای پلی‌مورفیک جایگزین شود.


3. Change Preventers


Divergent Change:

هرگونه تغییر در نحوه مدیریت توکن‌ها یا ساختار کلاس‌ها مستلزم تغییرات گسترده در متدهای متعدد مانند generatePhase2 و متدهای مرتبط است.


4. Dispensables


Comments:

وجود کامنت‌هایی که صرفاً توضیح‌دهنده‌ی عملکرد مستقیم کد هستند، مانند // else freeKeyword، می‌تواند نشانه‌ای از عدم خود مستند بودن کد باشد. با انتخاب نام‌های واضح‌تر برای متغیرها و متدها، می‌توان نیاز به این کامنت‌ها را کاهش داد.


Dead Code:

کدهای کامنت‌شده نظیر // else freeKeyword و بخش‌هایی که دیگر مورد استفاده قرار نمی‌گیرند باید حذف شوند.


Speculative Generality:

متدها و مقادیر خاصی وجود دارند که به نظر می‌رسد برای کاربردهای احتمالی آینده طراحی شده‌اند اما در حال حاضر استفاده‌ای ندارند، نظیر برخی شمارنده‌ها.


5. Couplers


Feature Envy:

برخی متدها مانند generatePhase2 بیش از حد به جزئیات داخلی DiagramInfo و LexicalAnalyzer وابسته هستند. این وابستگی‌ها را می‌توان با انتقال برخی وظایف به این کلاس‌ها کاهش داد.


Message Chains:

دسترسی زنجیره‌ای به متدها و مقادیر، مانند diagramInfo.isHaveDestructor(attribute.getValueType().getTypeName())، می‌تواند نشان‌دهنده‌ی وابستگی زیاد به ساختار داخلی کلاس‌های دیگر باشد.
