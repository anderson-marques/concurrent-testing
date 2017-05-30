# concurrent-testing
Easy TestRule to Load Testing

## Gettting started

### Add the dependency
```xml
    <dependency>
        <groupId>org.marques.anderson</groupId>
        <artifactId>concurrent-testing</artifactId>
        <version>1.0.0</version>
    </dependency>
```
 
 ### Use in your TestCases
 
 ```java
     package org.lite.concurrent.testing;
     
     import org.junit.Assert;
     import org.junit.Rule;
     import org.junit.Test;
     import ConcurrentTest;
     import ConcurrentTestsRule;
     
     /**
      * Concurrent tests examples
      */
     public class ExampleTest {
     
         /**
          * Create a new TestRule that will be applied to all tests
          */
         @Rule
         public ConcurrentTestsRule ct = ConcurrentTestsRule.silentTests();
     
         /**
          * Tests using 10 threads and make 20 requests. This means until 10 simultaneous requests.
          */
         @Test
         @ConcurrentTest(requests = 20, threads = 10)
         public void testConcurrentExecutionSuccess(){
             Assert.assertTrue(true);
         }
     
         /**
          * Tests using 10 threads and make 20 requests. This means until 10 simultaneous requests.
          */
         @Test
         @ConcurrentTest(requests = 200, threads = 10, timeoutMillis = 100)
         public void testConcurrentExecutionSuccessWaitOnly100Millissecond(){
         }
     
         @Test(expected = RuntimeException.class)
         @ConcurrentTest(requests = 3)
         public void testConcurrentExecutionFail(){
             throw new RuntimeException("Fail");
         }
     }
 ```
 
 #### ConcurrentTestsRule.silentTests()
  
  Runs all the tests in silent mode. Without feedback about requests; 
  
 #### ConcurrentTestsRule.verboseTests()
 
  Runs all the tests printing executions. With feedback about requests;
  
  ### Licence: MIT License
  
