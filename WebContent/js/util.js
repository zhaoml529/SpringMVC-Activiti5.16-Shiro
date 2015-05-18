function doSearch(currentPage)
{
	var pageNum = document.getElementById("pageNum").value;
	if(isNaN(pageNum))
	{
		alert("请输入正确的行数!");
	}
	else
	{
		document.getElementById('currentPage').value = currentPage;
		document.forms[0].submit();
	}
}