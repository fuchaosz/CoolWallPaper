package com.fuchao.pulltorefreshloadlayout.pullableview;

/**
 * 判断是否可以上拉或者下拉的接口，因为不同的View能否上下拉是由view本身决定的，layout是不能决定的
 * 例如：listView要在显示到最后一条才能上拉，但是TextView则没有限制
 *
 * @author fuchao
 */
public interface Pullable {
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    boolean canPullDown();

    /**
     * 判断是否可以上拉，如果不需要上拉功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    boolean canPullUp();
}
