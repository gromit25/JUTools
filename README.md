# JUTools(Java Utility Tools)
Java 관련 Utility 기능 모음     
JDK 1.8 기준으로 작성     
주의) lombok:1.18.28 을 사용하고 있음. IDE에 설정 필요    
@author: jmsohn(gromit25@hanmail.net)

### StringUtil    
----------------------------------    
> 문자열 처리 Utility
```java
StringUtil.escape(str);                // str="test\t입니다." -> "test	입니다."로 변환, 입력 받는 문자열에 이스케이프 문자열이 있는 경우 사용
StringUtil.replaceHtmlEntity(str);     // 문자열의 html 엔터티(<>& 등 -> &amp;lt;&amp;gt;&amp;amp; 등)를 변경, XSS 해결용
StringUtil.replaceEnterToBr(str);      // 문자열(contents) 내에 "(\r)\n" -> "<br>\r\n"로 변경
StringUtil.isValidFileName(fileName);  // 파일명이 유효한지 검증하는 메소드
StringUtil.parseHostPort("192.168.0.1:8080");  // "192.168.0.1:8080" -> { "192.168.0.1", "8080" }

StringUtil.hasNull(str);               // 문자열 내에 null(\0)가 포함 여부 반환
StringUtil.length(str);                // 문자열 길이 반환, null일 경우 0 반환
StringUtil.isEmpty(str);               // 문자열이 null 이거나 "" 일 경우 true 반환

StringUtil.join("hello", " ", "world");// 문자열 결합 "hello world"
StringUtil.reverse(str);               // 문자열 역전 "abc" -> "cba"
StringUtil.split(str, ',');            // 문자로 문자열을 나눔
StringUtil.splitFirst(str, ">");       // str: "abc>def>ghi", delimiter: ">" 일 경우,
                                       // -> {"abc", "def>ghi"} 반환
StringUtil.splitLast(str, ">");        // str: "abc>def>ghi", delimiter: ">" 일 경우,
                                       // -> {"abc>def", "ghi"} 반환


// 주어진 문자열을 delimiter로 나눈 후 특정 위치들의 문자열들을 선택하여 반환
// str: test1\ttest2\ttest3, delimiter: \t, locs: {0, 2, 1} -> {"test1", "test3", "test2"} 가 반환됨
String[] splited = StringUtil.pick(str, '\t', new int[]{0, 2, 1});
System.out.println(splited[0]);        // "test1" 출력

// 문자열 내 여러 문자열을 검색
String msg = "hello world!"; 
int[] result = StringUtil.find(msg, false, "hello", "world");
			
System.out.println(result[0]);         // 0 출력
System.out.println(result[1]);         // 6 출력

// 문자열 내 문자열 중 포함 된 것이 있는지 여부 반환
StringUtil.containsAny(msg, false, "hello", "world");

// 와일드 카드 패턴 매치
// "*문?" -> "을지문덕"에 매치
StringUtil.matchWildcard(msg, pattern);

// 문자열은 불변이나 보안상의 이유(메모리 해킹 등)로
// 문자열을 내부의 내용을 toStr로 수정함
StringUtil.changeStr(str, toStr);

// 문자열 내에 ${변수}을 변수 목록(vars)에서 찾아 대체함
// str: "${count}회 발생하였습니다." vars: "count": "2" -> "2회 발생하였습니다."
StringUtil.replaceVars(str, vars);

// 문자열 내에 ${변수} 목록을 반환
// str: "${count}회 발생하였습니다." -> {"count"} 반환
StringUtil.findAllVars(str);

```

### BytesUtil   
----------------------------------    
> TCP/UDP 통신, File read/write 등에서 byte 배열을 다루기 위한 Utility   
```java
ByteUtil.concat(bytes1, bytes2);    // bytes1, bytes2 배열을 합쳐서 바이트 배열로 반환
ByteUtil.cut(bytes1, 10, 20);       // bytes1의 10번째 부터 20번째까지 잘라서 바이트 배열로 반환
ByteUtil.split(bytes1, bytes2);     // bytes1을 bytes2로 나우어 바이트 배열의 Array로 반환

BytesUtil.contains(bytes1, bytes2);  // bytes1 내에 bytes2가 포함되어 있는지 여부를 반환 
BytesUtil.indexOf(bytes1, bytes2);   // bytes1 내에 bytes2가 처음으로 발견되는 위치를 반환
BytesUtil.endsWith(bytes1, bytes2);  // bytes1이 bytes2로 끝나는지 여부를 반환

BytesUtil.readAllBytes(new File("C:\\test.txt"));   // 파일의 모든 내용을 읽어 바이트 배열로 반환(스트림도 가능)
BytesUtil.readNBytes(new File("C:\\test.txt"), 10); // 파일의 내용 중 N 바이트까지만 읽어 반환(스트림도 가능)

BytesUtil.strToBytes(str);           // "1A03" -> byte[] {26, 3}
BytesUtil.bytesToStr(bytes1);        // byte[] {26, 3} -> "1A03"
```

### NIOBufferUtil   
----------------------------------    
> NIO Buffer의 메소드 중 일부가 1.8 버전에 없는 경우가 있어     
> 버전에 상관없이 지원하기 위한 용도로 만듦    
> JDK 1.8 이상에서 해당 메소드를 지원하면, 해당 메소드를 호출하도록 함
```java
NIOBufferUtil.flip(buffer);
NIOBufferUtil.clear(buffer);
```

### FileChannelUtil   
----------------------------------    
> 파일 채널을 통한 read/write Utility    
* read    
```java
try(
    FileChannelUtil channel = new FileChannelUtil(file, StandardOpenOption.READ);
) {
    String read = null;
    while((read = channel.readLine()) != null) {
        // 읽은 문자열 처리
        System.out.println(read);
    }
}
```
```java
try(
    FileChannelUtil channel = new FileChannelUtil(file, StandardOpenOption.READ);
) {
    channel.readLine(read -> {
        // 읽은 문자열 처리
        System.out.println(read);
    });
}
```
* write   
```java
// write할 파일
File file = new File("resources/write_test.txt");
// 파일 채널 오픈 옵션
OpenOption[] options = {StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE};

try(
    FileChannelUtil channel = new FileChannelUtil(file, options);
) {
    channel.write("안녕하세요.");
}
```

### EnvUtil   
----------------------------------
> 환경변수를 클래스의 static 변수에 설정해 주는 Utility
```
SET NAME=HONG GIL-DONG
```

```java
public class Config {
    @Env(name = "NAME")          // "NAME" 환경변수 값을 Config.NAME에 설정하기 위한 어노테이션
    public static String NAME;
}

public class Test {
    public static void main(String[] args) throws Exception {
        EnvUtil.set(Config.class);       // 환경 변수
        System.out.println(Config.NAME); // "HONG GIL-DONG"을 출력
    }
}
```

### SysUtil   
----------------------------------    
> 시스템 정보 관련 Utility   
```java
SysUtil.getHostname();      // 컴퓨터 이름 반환
SysUtil.getIps();           // ip 목록 반환(ipv4만)
SysUtil.getSysPerp();       // 현재 시스템 성능 정보를 JSON 형태로 반환
                            // 시스템 성능 정보 : cpu, memory, disk 사용율
                            
SysUtil.execute(cmd);       // 시스템 command를 실행하고 결과 반환
                            // cmd = "cmd /c dir \"C:\\Program Files\""
```

### CipherUtil   
----------------------------------    
> 암복호화 Utility    
> * 양방향 암복호화 : AES 및 AES 키 생성
> * 단방향 암호화 : SHA   
>
* AES
```java
// AES 키 생성
String key = CipherUtil.genAES128Key();
// 암복호화할 문장
String msg = "테스트 입니다.";

// AES 암호화
String encryptedMsg = CipherUtil.encryptAES(key, msg);
System.out.println(encryptedMsg);

// AES 복호화
String decryptedMsg = CipherUtil.decryptAES(key, encryptedMsg);
System.out.println(decryptedMsg);
```
* SHA
```java

// 암호화할 문장
String msg = "Hello World";

// SHA 암호화 수행
String encryptedMsg = CipherUtil.encryptSHA512(msg);
System.out.println(encryptedMsg);
```

### CronJob   
----------------------------------    
> 일정시간 간격으로 Thread를 실행시키는 Utility    
> 리눅스 크론잡과 동일한 형식으로 설정 가능    
> 형식 : (*) * * * * *   
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;초 분 시 일 월 주    
> &nbsp;&nbsp;&nbsp;초 : optional   
> &nbsp;&nbsp;&nbsp;주 : 0-7까지 지정 가능, 0과 7은 일요일, 1부터 월요일, 6은 토요일
>    
> 시간 표시 형식   
> * 숫자    
> ex) 10 * * * * -> 매시간 10분 마다 수행: 5시 10분, 6시 10분 ...    
> * 숫자(콤마)    
> ex) 10,20 * * * * -> 매시간 10분과 20분 마다 수행: 5시 10분, 5시 20분, 6시 10분 ....    
> * 범뮈    
> ex) 10-12 * * * * -> 매시간 10분, 11분, 12분 마다 수행: 5시 10분, 5시 11분, 5시 12분, 6시 10분 ....   
> * 반복    
> ex) */20 * * * * -> 매시간 20분 간격으로 수행: 5시 0분, 5시 20분, 5시 40분, 6시...    

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

### CacheService   
----------------------------------    
> 캐시 서비스 Utility    
> 자주 사용되는 코드성 데이터를 DB나 File에서 매번 읽어오지 않고, 일정기간 메모리에 저장 및 필요시 반환    
```java
// 데이터 - 실제는 DB나 File을 활용    
HashMap<String, String> values = new HashMap<>();    
values.put("test1", "test1 value");    
values.put("test2", "test2 value");    
		
Cache<String> cache = CacheService    
    .create(    
        (key) -> {    
            return values.get(key);  // 실제로는 키별로 DB에서 가져오는 코드 작성    
        },    
        1000,            // 유지 시간(단위:ms)    
        "* * * * * *"    // 유지 시간이 만료된 데이터 삭제 주기(CronJob 사용)    
    )
    .putAll(values);    
    
System.out.println(cache.get("test1"));  // "test1 value" 가 출력됨    
```    

### FileTracker   
----------------------------------    
> 파일의 변경사항에 대해 추적(Tracking)하는 Utility     
> tail -f 와 같은 기능임, 로그 추적 등에서 활용    
```java
FileTracker
    .create("C:\\test.log")
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
> DOM(Document Object Model) 파서를 기반으로 개발   
> 주요 특징   
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

// book 테그의 title에 설정된 text 출력
for(XMLNode book: books) {
    System.out.println(book.selectFirst("title").getText());
}
```

### PublishUtil
----------------------------------    
> 출력 Utility (Console, txt 파일, 엑셀 파일 등)    
> XML에 정의에 형태로 출력 수행    
> presspublisher 프로젝트를 통합 진행 중, presspublisher는 삭제 예정    
#### 콘솔 출력
* 출력 format xml file(resources/publisher/testformat.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<format>
	|   TEST MESSAGE - XML FORMAT
	|   --------------------------------------------------------------------------
	<foreach element="message" listExp="messages">
	|      <style type="FG_WHITE_BRIGHT"><print exp="message"/></style>
	</foreach>
	|   --------------------------------------------------------------------------
</format>
```
* java 프로그램
```java
// 출력할 메시지 설정
ArrayList<String> messages = new ArrayList<String>();
messages.add("test message 1");
messages.add("test message 2");
		
Map<String, Object> values = new HashMap<>();
values.put("messages", messages);
		
try {

    // 출력 format file		
    File formatFile = new File("resources/publisher/testformat.xml");
    // 화면 출력 실행
    PublishUtil.toConsole(formatFile, values);

} catch(Exception ex) {
    ex.printStackTrace();
}
```
* 출력
```
   TEST MESSAGE - XML FORMAT
   --------------------------------------------------------------------------
      test message 1
      test message 2
   --------------------------------------------------------------------------
```

#### 엑셀 출력
* 엑셀 출력 format xml file(resources/publisher/testExcelformat.xml)    
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workbook>

	<font name="테이블컬럼명폰트" fontname="맑은 고딕" bold="true"/>
	
	<cellstyle name="테이블헤더스타일" font="테이블컬럼명폰트" wrap="true">
		<border style="THIN" side="ALL" color="#000000"/>
		<background color="#D9D9D9"/>
		<alignment horizontal="CENTER" vertical="CENTER"/>
	</cellstyle>
	<cellstyle name="테이블데이터스타일">
		<border style="THIN" side="ALL" color="#000000"/>
	</cellstyle>

	<worksheet name="test 입니다.">

		<set name="baseRow" exp="1"/>
		<set name="baseColumn" exp="1"/>

		<cell position="baseRow:baseColumn" size=":200" style="테이블헤더스타일">
			|메세지들
		</cell>
		<foreach element="message" listExp="messages">
			<cell position="baseRow+1+_index_message:baseColumn" style="테이블데이터스타일">
				|<print exp="message"/>
			</cell>
		</foreach>
		
	</worksheet>
	
</workbook>
```
* java 프로그램    
```java
// 출력할 메시지 설정
ArrayList<String> messages = new ArrayList<String>();
messages.add("test message 1");
messages.add("test message 2");

Map<String, Object> values = new HashMap<>();
values.put("messages", messages);

try {

    // 출력 format file
    File formatFile = new File("resources/publisher/testExcelformat.xml");
    // 출력할 엑셀 파일
    File outFile = new File("C:\\data\\publish\\test.xlsx");
    
    // 엑셀 파일 출력 실행
    PublishUtil.toExcel(formatFile, outFile, values);
		
} catch(Exception ex) {
    ex.printStackTrace();
}
```
* 엑셀 파일 출력 결과   
![엑셀출력화면](https://github.com/gromit25/jutools/blob/main/resources/readmeimg/%EC%97%91%EC%85%80%EC%B6%9C%EB%A0%A51.png)

#### 엑셀 출력 - 라인차트   
* 엑셀 라인차트 출력 format xml file(resources/publisher/testExcelLineChart.xml)    
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workbook>
	<worksheet name="Line Chart">
		<cursor position="0:0"/>
		<rowcells>
			<cell>|Russia</cell>
			<cell>|Canada</cell>
			<cell>|USA</cell>
			<cell>|China</cell>
			<cell>|Brazil</cell>
			<cell>|Australia</cell>
			<cell>|India</cell>
		</rowcells>
		<rowcells>
			<cell type="NUMERIC">|17098242</cell>
			<cell type="NUMERIC">|9984670</cell>
			<cell type="NUMERIC">|9826675</cell>
			<cell type="NUMERIC">|9596961</cell>
			<cell type="NUMERIC">|8514877</cell>
			<cell type="NUMERIC">|7741220</cell>
			<cell type="NUMERIC">|3287263</cell>
		</rowcells>
		<rowcells>
			<cell type="NUMERIC">|14590041</cell>
			<cell type="NUMERIC">|35151728</cell>
			<cell type="NUMERIC">|32993302</cell>
			<cell type="NUMERIC">|14362887</cell>
			<cell type="NUMERIC">|21172141</cell>
			<cell type="NUMERIC">|25335727</cell>
			<cell type="NUMERIC">|13724923</cell>
		</rowcells>

		<chart
			title="Area-wise Top Seven Countries"
			range="4:0~26:7">
		
			<axes>
				<category-axis position="BOTTOM" title="Country"/>
				<value-axis position="LEFT" title="Area &amp; Population"/>
			</axes>
			
			<line-series title="Area" smooth="false" mark-style="STAR">
				<category-ds range="0:0~0:6" type="STRING"/>
				<value-ds range="1:0~1:6" type="NUMBER"/>
			</line-series>
			
			<bar-series title="Population" direction="COL">
				<category-ds range="0:0~0:6" type="STRING"/>
				<value-ds range="2:0~2:6" type="NUMBER"/>
			</bar-series>
			
		</chart>
		
	</worksheet>
</workbook>
```
* java 프로그램    
```java
try {

    // 출력 format file
    File formatFile = new File("resources/publisher/testExcelLineChart.xml");
    // 출력할 엑셀 파일
    File outFile = new File("C:\\data\\publish\\testLineChart.xlsx");

    // 엑셀 파일 출력 실행
    PublishUtil.toExcel(formatFile, outFile);

} catch(Exception ex) {
    ex.printStackTrace();
}
```
* 엑셀 파일 출력 결과   
![엑셀출력화면](https://github.com/gromit25/jutools/blob/main/resources/readmeimg/%EC%97%91%EC%85%80%EB%9D%BC%EC%9D%B8%EC%B0%A8%ED%8A%B8%EC%B6%9C%EB%A0%A51.png)

#### 엑셀 출력 - 파이차트   
* 엑셀 파이차트 출력 format xml file(resources/publisher/testExcelPieChart.xml)    
```xml
<?xml version="1.0" encoding="UTF-8"?>
<workbook>
	<worksheet name="Line Chart">
		<cursor position="0:0"/>
		<rowcells>
			<cell>|Russia</cell>
			<cell>|Canada</cell>
			<cell>|USA</cell>
			<cell>|China</cell>
			<cell>|Brazil</cell>
			<cell>|Australia</cell>
			<cell>|India</cell>
		</rowcells>
		
		<rowcells>
			<cell type="NUMERIC">|17098242</cell>
			<cell type="NUMERIC">|9984670</cell>
			<cell type="NUMERIC">|9826675</cell>
			<cell type="NUMERIC">|9596961</cell>
			<cell type="NUMERIC">|8514877</cell>
			<cell type="NUMERIC">|7741220</cell>
			<cell type="NUMERIC">|3287263</cell>
		</rowcells>

		<chart
			title="Area-wise Top Seven Countries"
			range="4:0~26:7">
			
			<legend position="RIGHT"/>
			
			<pie-series title="Area">
				<category-ds range="0:0~0:6" type="STRING"/>
				<value-ds range="1:0~1:6" type="NUMBER"/>
			</pie-series>
		</chart>
	</worksheet>
</workbook>
```
* java 프로그램    
```java
try {

    // 출력 format file
    File formatFile = new File("resources/publisher/testExcelPieChart.xml");   
    // 출력할 엑셀 파일
    File outFile = new File("C:\\data\\publish\\testPieChart.xlsx");
    
    // 엑셀 파일 출력 실행
    PublishUtil.toExcel(formatFile, outFile);
    
} catch(Exception ex) {
    ex.printStackTrace();
}
```
* 엑셀 파일 출력 결과   
![엑셀출력화면](https://github.com/gromit25/jutools/blob/main/resources/readmeimg/%EC%97%91%EC%85%80%ED%8C%8C%EC%9D%B4%EC%B0%A8%ED%8A%B8%EC%B6%9C%EB%A0%A51.png)


### MathUtil
----------------------------------    
> 수학식 관련 Utility    
> 주요 기능   
> * 설정 파일등에 수학식을 이용하여 값을 설정할 수 있도록 지원하기 위함    
> 예를 들어, 로그 파일 최대 사이즈 : 5 * 1024 KiB -> 5242880, "B" 로 분리   
```java
MathResult result = MathUtil.calculateWithUnit("3*4/ 2 + -2 * 2.5 MiB");

System.out.println(result.getValue());      // "1048576" 출력
System.out.println(result.getBaseUnit());   // "B" 출력
```

### StatUtil
----------------------------------    
> 통계 관련 Utility    
> 주요기능    
> * 실시간 모수(Parameter)/통계치(Statistic) 계산 기능    
>   평균, 분산, 표준편차, 왜도(skewness), 첨도(kurtosis) 계산    
>   계산을 위해 모든 데이터를 저장하게 되면 메모리 소요가 증가하기 떄문에    
>   데이터의 합, 제곱합, 세제곱합, 네제곱합과 개수 데이터만 가지고 실시간으로 계산함    
> * 실시간 6시그마 통제도(Control Chart)의 넬슨룰 위반 여부 검사 기능    
>   검사시에 카운팅을 bit 연산으로 하여 메모리 사용을 최소화    
>   [Nelson rules](https://en.wikipedia.org/wiki/Nelson_rules) 참고

```java
// 모수 계산 객체 생성
RTParameter rtparam = StatUtil.createRTParameter();
// 통계량 계산 객체 생성
RTStatistic rtstat = StatUtil.createRTStatistic();
// 넬슨룰 검사 객체 생성 - 여기에서는 rule1과 rule2 만 검사
NelsonRule nrule = StatUtil.createNelsonRule(10, 1, NelsonRule.RULE1|NelsonRule.RULE2);

for(double data: dataList) {

    // 데이터의 모수와 통계량 계산
    rtparam.add(data);
    rtstat.add(data);

    // 모수와 통계량의 개수, 합계, 평균, 분산, 표준편차, 왜도, 첨도를 JSON 형태로 출력
    System.out.println(rtparam);
    System.out.println(rtstat);

    // 넬슨룰 검사
    ArrayList<Integer> violatedRules = nrule.check(data);
    for(Integer ruleIndex: violatedRules) {
        // 넬슨룰 위반 처리
        System.out.println(ruleIndex);
    }
}
```

