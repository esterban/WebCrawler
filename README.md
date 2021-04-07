# WebCrawler

## Implementation Notes
* JDK used - openjdk-11.0.2_windows-x64_bin (downloaded manually via the Oracle website)
* Build tools - Gradle 6.7 (as specified in gradle-wrapper.properties, gradle/wrapper/gradle-wrapper.jar included)
* Developed / tested on Windows using
  - IntelliJ IDEA 2020.3.3 (Community Edition)
    - Build #IC-203.7717.56, built on March 15, 2021
    - Runtime version: 11.0.10+8-b1145.96 amd64 (IntelliJ executing using this, not build nor running project code)
    - VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
    - Windows 10 10.0
    - GC: ParNew, ConcurrentMarkSweep
    - Memory: 1963M
    - Cores: 8
    - Registry: debugger.watches.in.variables=false
    - Non-Bundled Plugins: org.jetbrains.kotlin
  - Command line used to run cygwin


## Assumptions
* Circular dependencies are detected if a page i.e. http://a.com/a.html, has already been visited and will not be visited again
* http and https links are equivalent - they are treated as duplicates and not followed more than once
* The query component of a URI is ignored when recognising circular dependencies
  - i.e. http://a.com/a.html?query=value is the viewed the same as http://a.com/a.html
* Domain extensions are treated differently with regard to recognising internal / external links
  - When trawling www.bbc.com - links to www.bbc.co.uk are considered external
 * When considering if a page is internal or external ports are ignored 
   - https://wipro.com is the same as https://wipro.com:1111


## Performance Tradeoffs
* The WebCrawler is only single threaded to simplify the threading implementation
* Some MIME types are not supported by JSoup and throw an exception consequently


## Executing WebCrawler
* Running using <code>cygwin</code> with Windows 10
  * In project root to build
    - <code>./gradlew.bat clean build</code>
  * In project root to run
    - <code>./gradlew.bat run</code>
* Running using <code>cmd</code> with Windows 10
  * In project root to build
    - <code>gradlew.bat clean build</code>
  * In project root to run
    - <code>gradlew.bat run</code>
* The output is a JSON to STDOUT the attributes of a given page are output as,
  - Page address
  - List of internal links
  - List of external links
  - List of images


## Notes
* There is a static int value specified in <code>src/main/java/com/srwit/WebSpider.java</code> (TODO - put this into a properties file)
  - <code>private final static int debugMaxPageCounter = 100;</code>
* This limits the total number of web pages visited
  - Upon reflection this could have been more effectively achieved using a maximum depth limit instead of or complementing the maximum number of pages visited constraint 