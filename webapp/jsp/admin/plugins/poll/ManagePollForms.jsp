<jsp:useBean id="managePollForm" scope="session" class="fr.paris.lutece.plugins.poll.web.PollFormJspBean" />
<% String strContent = managePollForm.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
