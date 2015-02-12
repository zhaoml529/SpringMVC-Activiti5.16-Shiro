package com.zml.oa.pagination;

public class Pagination {
	protected String pageStr = "";
	protected Integer totalSum = 0;  	//共多少记录
	protected Integer totalPage = 0;	//共多少页
	protected Integer currentPage = 1;
	protected Integer prePage = 0;
	protected Integer nextPage = 0;	
	protected Integer pageNumDefault = 10;
	protected Integer pageNum = null;
	protected Integer firstResult = 0;
	protected Integer maxResult = 0;
	protected String orderStr;
	protected String orderColumn;
	protected String preName = "";
	
	public String getOrderStr() {
		return orderStr;
	}
	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public Integer getFirstResult() {
		return firstResult;
	}
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	public Integer getPageNumDefault() {
		return pageNumDefault;
	}
	public void setPageNumDefault(Integer pageNumDefault) {
		this.pageNumDefault = pageNumDefault;
	}
	
	public String getPreName() {
		return preName;
	}
	public void setPreName(String preName) {
		this.preName = preName;
	}
	public Integer getPageNum() {
		if(this.pageNum == null) {
			this.pageNum = this.getPageNumDefault();
		}
		pageNum = pageNum < 1 || pageNum > 1000 ? 10 : pageNum;
		return pageNum;
	}
	
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public String getPageStr() {
		return pageStr;
	}
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getPrePage() {
		return prePage;
	}
	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}
	public Integer getNextPage() {
		return nextPage;
	}
	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}
	public Integer getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(Integer totalSum) {
		this.totalSum = totalSum;
	}
	//处理分页
	public void processTotalPage()
	{
		this.setTotalPage(new Integer((this.getTotalSum() / this.getPageNum()) + (this.getTotalSum() % this.getPageNum() != 0 ? 1 : 0)));
		this.setPrePage(this.getCurrentPage() - 1 > 1 ? this.getCurrentPage() - 1 : 1);
		this.setNextPage(this.getCurrentPage() + 1 < this.getTotalPage() ? this.getCurrentPage() + 1 : this.getTotalPage());
		this.setFirstResult(this.getCurrentPage() == 1 ? 0 : this.getPrePage() * this.getPageNum());
		this.setMaxResult(this.getPageNum() > this.getTotalSum() ? this.getTotalSum() : this.getPageNum());
		
		StringBuffer str = new StringBuffer();
		str.append("<table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"box6\">");
		str.append("<tr><td width=\"100%\" height=\"24\"  class=\"bt\">");
		str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" background=\"\">");
		str.append("<input type=\"hidden\" name=\"current\" value=\"\">");
		str.append("<tr>");
		
		str.append("<td align=\"left\" valign=\"center\" class=\"bt\" width=\"30%\">共" + this.getTotalPage() + "页&nbsp;");
		str.append("第" + this.getCurrentPage() + "页&nbsp;");
		str.append("每页<input id=\"pageNum\" name=\"" + this.preName + "pageNum\" type=\"text\" class=\"bt\" value=\""  + this.getPageNum() + "\" onChange=\"doSearch(1);\"  maxlength=\"3\" size=\"2\">条记录</td>");
		str.append("<td align=\"right\" valign=\"center\" class=\"bt\" width=\"70%\">");
		
		if(this.getTotalPage().longValue() > 1) {
			str.append("<a href=\"javascript:doSearch(1);\" class=\"bt\" onClick=\"\">首页</a>&nbsp;&nbsp;");
			str.append("<a href=\"javascript:doSearch(" + this.getPrePage() + ");\" class=\"bt\" onClick=\"\" style=\"cursor:hand\">上一页</a>&nbsp;&nbsp;");
			str.append("<a href=\"javascript:doSearch(" + this.getNextPage() + ");\" class=\"bt\" onClick=\"\" style=\"cursor:hand\">下一页</a>&nbsp;");
			str.append("<a href=\"javascript:doSearch(" + this.getTotalPage() + ");\" class=\"bt\" onClick=\"\" style=\"cursor:hand\"> 末页 </a>&nbsp;");
		} else {
			str.append("首页&nbsp;&nbsp;");
			str.append("上一页&nbsp;&nbsp;");
			str.append("下一页&nbsp;");
			str.append("末页&nbsp;");
		}
		str.append("共" + this.getTotalSum() + "条记录<input type=\"hidden\" id=\"currentPage\" name=\"" + this.preName + "currentPage\" value=\"" + this.getCurrentPage() + "\">");
		str.append("</td>");
		str.append("</tr>");
		str.append("</table>");
		str.append("</td></tr>");
		str.append("</table>");
		
        this.setPageStr(str.toString());
	}
}
