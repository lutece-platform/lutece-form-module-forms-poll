/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.poll.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import java.util.List;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.plugins.poll.business.PollFormHome;

/**
 * This is the business class test for the object PollForm
 */
public class PollFormJspBeanTest extends LuteceTestCase
{
    private static final int IDFORM1 = 1;
    private static final int IDFORM2 = 2;
    private static final boolean ISVISIBLE1 = true;
    private static final boolean ISVISIBLE2 = false;

    public void testJspBeans( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );

        // display admin PollForm management JSP
        PollFormJspBean jspbean = new PollFormJspBean( );
        String html = jspbean.getManagePollForms( request );
        assertNotNull( html );

        // display admin PollForm creation JSP
        html = jspbean.getCreatePollForm( request );
        assertNotNull( html );

        // action create PollForm
        request = new MockHttpServletRequest( );

        request.addParameter( "id_form", String.valueOf( IDFORM1 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE1 ) );
        request.addParameter( "action", "createPollForm" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createPollForm" ) );
        request.setMethod( "POST" );
        response = new MockHttpServletResponse( );
        AdminUser adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // display modify PollForm JSP
        request = new MockHttpServletRequest( );
        request.addParameter( "id_form", String.valueOf( IDFORM1 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE1 ) );
        List<Integer> listIds = PollFormHome.getIdPollFormsList( );
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new PollFormJspBean( );

        assertNotNull( jspbean.getModifyPollForm( request ) );

        // action modify PollForm
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.addParameter( "id_form", String.valueOf( IDFORM2 ) );
        request.addParameter( "is_visible", String.valueOf( ISVISIBLE2 ) );
        request.setRequestURI( "jsp/admin/plugins/example/ManagePollForms.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createPollForm, qui est l'action par défaut
        request.addParameter( "action", "modifyPollForm" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyPollForm" ) );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // get remove PollForm
        request = new MockHttpServletRequest( );
        // request.setRequestURI("jsp/admin/plugins/example/ManagePollForms.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new PollFormJspBean( );
        request.addParameter( "action", "confirmRemovePollForm" );
        assertNotNull( jspbean.getModifyPollForm( request ) );

        // do remove PollForm
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.setRequestURI( "jsp/admin/plugins/example/ManagePollFormts.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createPollForm, qui est l'action par défaut
        request.addParameter( "action", "removePollForm" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removePollForm" ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.setMethod( "POST" );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

    }
}
