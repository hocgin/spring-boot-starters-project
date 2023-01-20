package in.hocg.boot.utils.utils;

import cn.hutool.core.lang.Assert;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;

/**
 * Created by hocgin on 2020/2/15.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ValidUtils {

    public <T> T notNull(T object, String errorMsgTemplate, Object... params) {
        return Assert.notNull(object, errorMsgTemplate, params);
    }

    public <T> T notNull(T object) {
        return Assert.notNull(object);
    }

    public void isNull(Object object, String errorMsgTemplate, Object... params) {
        Assert.isNull(object, errorMsgTemplate, params);
    }

    public void isNull(Object object) {
        Assert.isNull(object);
    }

    public void isTrue(boolean expression, String errorMsgTemplate, Object... params) {
        Assert.isTrue(expression, errorMsgTemplate, params);
    }

    public void isTrue(boolean expression) {
        Assert.isTrue(expression);
    }

    public void isFalse(boolean expression, String errorMsgTemplate, Object... params) {
        Assert.isFalse(expression, errorMsgTemplate, params);
    }

    public void isFalse(boolean expression) {
        Assert.isFalse(expression);
    }

    public <T extends CharSequence> T notEmpty(T text, String errorMsgTemplate, Object... params) {
        return Assert.notEmpty(text, errorMsgTemplate, params);
    }

    public <T extends CharSequence> T notEmpty(T text) {
        return Assert.notEmpty(text);
    }

    public <T extends CharSequence> T notBlank(T text, String errorMsgTemplate, Object... params) {
        return Assert.notBlank(text, errorMsgTemplate, params);
    }

    public <T extends CharSequence> T notBlank(T text) {
        return Assert.notBlank(text);
    }

    public String notContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) {
        return Assert.notContain(textToSearch, substring, errorMsgTemplate, params);
    }

    public String notContain(String textToSearch, String substring) {
        return Assert.notContain(textToSearch, substring);
    }

    public Object[] notEmpty(Object[] array, String errorMsgTemplate, Object... params) {
        return Assert.notEmpty(array, errorMsgTemplate, errorMsgTemplate, params);
    }

    public Object[] notEmpty(Object[] array) {
        return Assert.notEmpty(array);
    }

    public <T> T[] noNullElements(T[] array, String errorMsgTemplate, Object... params) {
        return Assert.noNullElements(array, errorMsgTemplate, params);
    }

    public <T> T[] noNullElements(T[] array) {
        return Assert.noNullElements(array);
    }

    public <T> Collection<T> notEmpty(Collection<T> collection, String errorMsgTemplate, Object... params) {
        return Assert.notEmpty(collection, errorMsgTemplate, params);
    }

    public <T> Collection<T> notEmpty(Collection<T> collection) {
        return Assert.notEmpty(collection);
    }

    public <K, V> Map<K, V> notEmpty(Map<K, V> map, String errorMsgTemplate, Object... params) {
        return Assert.notEmpty(map, errorMsgTemplate, params);
    }

    public <K, V> Map<K, V> notEmpty(Map<K, V> map) {
        return Assert.notEmpty(map);
    }

    public <T> T isInstanceOf(Class<?> type, T obj, String errorMsgTemplate, Object... params) {
        return Assert.isInstanceOf(type, obj, errorMsgTemplate, params);
    }

    public <T> T isInstanceOf(Class<?> type, T obj) {
        return Assert.isInstanceOf(type, obj);
    }

    public void isAssignable(Class<?> superType, Class<?> subType, String errorMsgTemplate, Object... params) {
        Assert.isAssignable(superType, subType, errorMsgTemplate, params);
    }

    public void isAssignable(Class<?> superType, Class<?> subType) {
        Assert.isAssignable(superType, subType);
    }

    public void state(boolean expression, String errorMsgTemplate, Object... params) {
        Assert.state(expression, errorMsgTemplate, params);
    }

    public void state(boolean expression) {
        Assert.state(expression);
    }

    public int checkIndex(int index, int size, String errorMsgTemplate, Object... params) {
        return Assert.checkIndex(index, size, errorMsgTemplate, params);
    }

    public int checkIndex(int index, int size) {
        return Assert.checkIndex(index, size);
    }

    public Number checkBetween(Number value, Number min, Number max) {
        return Assert.checkBetween(value, min, max);
    }

    public double checkBetween(double value, double min, double max) {
        return Assert.checkBetween(value, min, max);
    }

    public long checkBetween(long value, long min, long max) {
        return Assert.checkBetween(value, min, max);
    }

    public int checkBetween(int value, int min, int max) {
        return Assert.checkBetween(value, min, max);
    }

    public void fail(String message) {
        throw new IllegalArgumentException(message);
    }

    public void fail(Exception e) {
        fail(e.getMessage());
    }

}
