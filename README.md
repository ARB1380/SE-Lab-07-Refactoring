# SE-Lab-07-Refactoring


## بازآرایی‌ها

### Facade #1
در متد main کد زیر را داشتیم:


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


