# JUTools(Java Utility Tools)
Java 관련 Utility 기능 모음    
주의) lombok:1.18.26 을 사용하고 있음. 개발툴에 설정 필요    

### BytesUtil   
----------------------------------    
> TCP/UDP 통신, File read/write 등에서 byte 단위로 다루기 위한 Utility   

### CronJob   
----------------------------------    
> 일정시간 간격으로 Thread를 실행시키는 Utility    
> 리눅스 크론잡과 동일한 형식으로 설정 가능
```java
CronJob.builder()
    .cronExp("0 1 * * *")  // 매 1시간 마다 수행
    .job(new Runnable() {
        @Override
        public void run() {
            // 수행할 내용
            System.out.println("Hello world");
        }
    })
    .build().run();
```

### FileTracker   
----------------------------------    
> 파일의 변경사항에 대해 추적(Tracking)하는 Utility
> tail -f 와 같은 기능임, 로그 추적 등에서 활용
```java
FileTracker.create("C:\\test.log")
    .tracking(
        msg -> {
            // 파일에 추가된 메시지 처리
            System.out.println(msg);
        }
    );
```

### XMLUtil    
----------------------------------    
> XML 문서의 tag에 대해 쉽게 접근할 수 있는 Utility
> DOM(Document Object Model)을 기반으로 개발됨
> select 메소드를 통해 XML 노드를 

```java
XMLArray books = XMLUtil
    .getRootNode("C:\\test.xml")
    // book 테그 이하에 author 테그 중 "일연"을 찾아 반환
    // 매칭 방식은 세가지 형태로 제공
    // - 완전 매칭 : '일연'
    // - 와일드 카드 매칭 : w'*지문?' -> "을지문덕" 매칭
    // - 정규표현식 매칭 : p'[0-9]{3}' -> "123" 매칭
    .select("book > auth*(#text='일연')")
    .getParents(); 

// book title 출력
for(XMLNode book: books) {
	System.out.println(book.selectFirst("title").getText());
}
```

