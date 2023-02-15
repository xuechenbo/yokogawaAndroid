package com.yokogawa.xc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yokogawa.xc.demo.bean.AccountData;
import com.yokogawa.xc.utils.ExamUtils;
import com.yokogawa.xc.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.meta.When;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void patternTest() {
        String pattern = "(^abc)(cde$)";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher("abcdefgcde");
        boolean b = matcher.find();
        if (b) {
            int i = matcher.groupCount();
            String group = matcher.group(0);
            String group1 = matcher.group(1);
        }
        String PHONE_NUMBER_REGEX = "(^1{1})(AAA)(\\d*$)";
        assertTrue("false", Pattern.matches("^(abc)\\d{7}", "abc48446541"));
    }


    @Test
    public void patt() {
        List mockedList = Mockito.mock(List.class);
        // 设置mock对象的行为 － 当调用其get方法获取第0个元素时，返回"one"
        when(mockedList.get(0)).thenReturn("one");
        // 使用mock对象 － 会返回前面设置好的值"one"，即便列表实际上是空的
        String str = (String) mockedList.get(0);
        Assert.assertTrue("one".equals(str));
        Assert.assertTrue(mockedList.size() == 0);
        // 验证mock对象的get方法被调用过，而且调用时传的参数是0
        Mockito.verify(mockedList).get(0);
    }

    @Mock
    AccountData accountData;
    @Mock
    ExamUtils examUtils;

    @Before
    public void setupAccountData() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsNotNull() {
        assertNotNull(accountData);
        assertNotNull(examUtils);
    }

    @Test
    public void testIsLogin() {
        when(accountData.isLogin()).thenReturn(true);
        boolean login = accountData.isLogin();
        assertTrue(login);
    }

    @Test
    public void ExamDemo() {
//        String noEmptStr = verify(examUtils).getNoEmptStr("abc dafds fa fads fasdf af");
//        assertEquals("abcdafdsfafadsfasdaf",noEmptStr);
        boolean flag = verify(examUtils).getSingleType("abdc", "abd,abc,abef");
        assertTrue(flag);
    }


    @Test
    public void PatternD() {
        assertTrue(Pattern.matches("\\$ITEM_NO\\d*", "$ITEM_NO11a"));
        Pattern.matches("\\d-.*(\\+\\d-.*)*", "");
        assertTrue(Pattern.matches("^1[5|3|8|9|6|7]\\d{9}", "156948042725"));
        assertTrue(Pattern.matches("(\\d{15})|(\\d{17}([0-9]|X))", "130727199410193932"));

        assertTrue(Pattern.matches(".*@qq.com$", "1@$%%^306765203@qq.com"));
        Pattern compile = Pattern.compile("www...");
        Matcher matcher = compile.matcher("wwwabc");
        if (matcher.find()) {
            String group = matcher.group(0);
        }
    }

    @Test
    public void PatternTest() {
        //6-16位字母和数字组合
        assertTrue(Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$", "a132145abc"));
        // --必须包含 数字,字母,符号 3项组合的 正则表达式
        assertTrue(Pattern.matches("^(?:(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[,\\.#%'\\+\\*\\-:;^_`].*))[,\\.#%'\\+\\*\\-:;^_`0-9A-Za-z]{8,10}$", "1113232"));


    }


    @Test
    public void patterTe() {
        /**
         * ?!(pattern)
         * ?:(pattern)
         * ?=(pattern)
         */
//        Pattern.matches("CD(?=CEF)", "CDABC");
//        assertTrue(Pattern.matches("Windows(?!95|98|NT|2000)", "Windows3.1"));

        Pattern compile = Pattern.compile("Windows(?!95|98|NT|2000)");
        Matcher matcher = compile.matcher("Windows3.1");
        assertTrue(matcher.find());


        Pattern compile1 = Pattern.compile("www(?:a|b|c|d)");
    }
}






























