# JUTools(Java Utility Tools)
Java 관련 Utility 기능 모음    
주의) lombok:1.18.26 을 사용하고 있음. IDE에 설정 필요    

### BytesUtil   
----------------------------------    
> TCP/UDP 통신, File read/write 등에서 byte 단위로 다루기 위한 Utility   

### CronJob   
----------------------------------    
> 일정시간 간격으로 Thread를 실행시키는 Utility    
> 리눅스 크론잡과 동일한 형식으로 설정 가능
```java
CronJob.builder()
    .cronExp("0 1,2 * * *")  // 매일 1시, 2시에 수행
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
========================================    
> XML 문서의 tag에 대해 쉽게 접근할 수 있는 Utility   
> DOM(Document Object Model)을 기반으로 개발   
> * 주요 특징   
> * select 메소드를 통해 XML 노드를 선택 및 조회   
> 포맷) 테그1 > 테그2(속성1='값') > ...   
> * 테그와 속성은 와일드 카드 매칭을 기본으로 함   
> * 값의 매칭은 아래의 세가지 형태를 제공함   
> 1 완전 매칭 : '일연'   
> 2 와일드 카드 매칭 : w'*지문?' -> "을지문덕" 매칭   
> 3 정규표현식 매칭 : p'[0-9]{3}' -> "123" 매칭   

```java
XMLArray books = XMLUtil
    .getRootNode("C:\\test.xml")
    .select("book > auth*(#text='일연')") // book 테그 이하에 author 테그 중 "일연"을 찾아 반환   
    .getParents(); 

// book title 출력
for(XMLNode book: books) {
	System.out.println(book.selectFirst("title").getText());
}
```

### StatUtil