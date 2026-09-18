 package fr.paris.lutece.plugins.poll.web;

import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

import java.util.List;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the business class test for the object PollForm
 */
public class PollFormJspBeanTest extends LuteceTestCase
{
    private static final int IDFORM1 = 1;
    private static final int IDFORM2 = 2;
    private static final boolean ISVISIBLE1 = true;
    private static final boolean ISVISIBLE2 = false;

    @Inject
    private PollFormJspBean _jspbean;
    @Inject
    private Models _models;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        CommonsService.activateCommons( CommonsService.getCurrentCommonsKey( ) );
    }

    @Test
    public void testJspBeans( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );

        // display admin PollForm management JSP
        String html = _jspbean.getManagePollForms( request, _models );
        assertNotNull( html );

        // display admin PollForm creation JSP
        html = _jspbean.getCreatePollForm( request, _models );
        assertNotNull( html );

        // action create PollForm
        request = new MockHttpServletRequest( );

        request.addParameter( "id_form", String.valueOf( IDFORM1 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE1 ) );
        request.addParameter( "action", "createPollForm" );
        request.setMethod( "POST" );
        response = new MockHttpServletResponse( );
        AdminUser adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminUserUtils.registerAdminUserWithRight( request, adminUser, PollFormJspBean.RIGHT_MANAGE );
            html = _jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }

        // display modify PollForm JSP
        request = new MockHttpServletRequest( );
        request.addParameter( "id_form", String.valueOf( IDFORM1 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE1 ) );
        List<Integer> listIds = PollFormHome.getIdPollFormsList( );
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, adminUser, PollFormJspBean.RIGHT_MANAGE );

        assertNotNull( _jspbean.getModifyPollForm( request, _models ) );

        // action modify PollForm
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.addParameter( "id_form", String.valueOf( IDFORM2 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE2 ) );
        request.setRequestURI( "jsp/admin/plugins/poll/ManagePollForms.jsp" );
        // important pour que MVCController sache quelle action effectuer
        request.addParameter( "action", "modifyPollForm" );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminUserUtils.registerAdminUserWithRight( request, adminUser, PollFormJspBean.RIGHT_MANAGE );
            html = _jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }

        // get confirm remove PollForm view
        request = new MockHttpServletRequest( );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.addParameter( "view", "confirmRemovePollForm" );
        AdminUserUtils.registerAdminUserWithRight( request, adminUser, PollFormJspBean.RIGHT_MANAGE );
        html = _jspbean.processController( request, new MockHttpServletResponse( ) );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );

        // do remove PollForm
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.setRequestURI( "jsp/admin/plugins/poll/ManagePollForms.jsp" );
        // important pour que MVCController sache quelle action effectuer
        request.addParameter( "action", "removePollForm" );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.setMethod( "POST" );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminUserUtils.registerAdminUserWithRight( request, adminUser, PollFormJspBean.RIGHT_MANAGE );
            html = _jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }

    }
}
