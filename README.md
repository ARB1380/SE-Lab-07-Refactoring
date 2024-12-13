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


