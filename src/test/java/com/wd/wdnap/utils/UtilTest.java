package com.wd.wdnap.utils;


import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UtilTest {
 
 @Test
 public void testIsNullOrEmpty(){
  assertThat(Util.isNullOrEmpty(""), is(true));
  assertThat(Util.isNullOrEmpty("test"), is(false));
  assertThat(Util.isNullOrEmpty(null), is(true));
 }

 @Test
 public void testDecrypt(){
  assertEquals(Util.decrypt("qyUbWzX4Es8GNOTApllMpnHpt7X2ydWT", "dev"), "estore-sync");
 }
 
 

 
 

 
}
