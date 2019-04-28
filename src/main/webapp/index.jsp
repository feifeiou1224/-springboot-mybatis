
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/common/backend_common.jsp"/>
</head>

<div class="navbar-buttons navbar-header pull-right" role="navigation">
    <ul class="nav ace-nav">

        <li class="light-blue dropdown-modal">

            <a class="dropdown-toggle" data-toggle="dropdown" href="#" >
                        <span class="user-info">
                            <small>欢迎,${sessionScope.user.username}</small>
                        </span>
                <i class="ace-icon fa fa-caret-down"></i>
            </a>

            <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">

                <li>
                    <a href="#"> <i class="ace-icon fa fa-cog"></i>
                        设置
                    </a>
                </li>

                <li>
                    <a href="profile.html"> <i class="ace-icon fa fa-user"></i>
                        个人资料
                    </a>
                </li>

                <li class="divider"></li>

                <li>
                    <a href="/logout.page"> <i class="ace-icon fa fa-power-off"></i>
                        注销
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</div>

</html>
