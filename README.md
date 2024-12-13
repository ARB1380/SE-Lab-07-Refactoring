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
`` java
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

## سوالات


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
