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

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.forms.business.Step;
import fr.paris.lutece.plugins.forms.business.StepHome;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.poll.business.PollData;
import fr.paris.lutece.plugins.poll.business.PollForm;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.plugins.poll.business.PollFormQuestion;
import fr.paris.lutece.plugins.poll.business.PollFormQuestionHome;
import fr.paris.lutece.plugins.poll.business.PollVisualization;

/**
 * This class provides the user interface to manage PollForm features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManagePollForms.jsp", controllerPath = "jsp/admin/plugins/poll/", right = "POLL_MANAGEMENT" )
public class PollFormJspBean extends AbstractManageJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = -1272407869100208041L;
    // Templates
    private static final String TEMPLATE_MANAGE_POLLFORMS = "/admin/plugins/poll/manage_pollforms.html";
    private static final String TEMPLATE_CREATE_POLLFORM = "/admin/plugins/poll/create_pollform.html";
    private static final String TEMPLATE_MODIFY_POLLFORM = "/admin/plugins/poll/modify_pollform.html";
    private static final String TEMPLATE_MODIFY_POLLFORM_QUESTION = "/admin/plugins/poll/modify_pollform_question.html";
    private static final String TEMPLATE_VIEW_CHARTS = "/admin/plugins/poll/view_charts.html";

    // Parameters
    private static final String PARAMETER_ID_POLLFORM = "id";
    private static final String PARAMETER_ID_POLLFORM_QUESTION = "id_question";
    private static final String PARAMETER_ID_POLL = "id_poll";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_POLLFORMS = "poll.manage_pollforms.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_POLLFORM = "poll.modify_pollform.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_POLLFORM = "poll.create_pollform.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_POLLFORM_QUESTION = "poll.modify_pollform_question.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_VIEW_CHARTS = "poll.view_charts.pageTitle";

    // Markers
    private static final String MARK_POLLFORM_LIST = "pollform_list";
    private static final String MARK_POLLFORM = "pollform";
    private static final String MARK_POLL_FORM_QUESTION = "pollform_question";
    private static final String MARK_FORM_LIST = "form_list";
    private static final String MARK_FORM = "form";
    private static final String MARK_FORM_STEP_QUESTION_LIST = "form_step_question_list";
    private static final String MARK_POLL_FORM_QUESTION_LIST = "poll_form_question_list";
    private static final String MARK_QUESTION = "question";

    private static final String JSP_MANAGE_POLLFORMS = "jsp/admin/plugins/poll/ManagePollForms.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_POLLFORM = "poll.message.confirmRemovePollForm";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "poll.model.entity.pollform.attribute.";

    // Views
    private static final String VIEW_CHARTS = "viewCharts";

    private static final String VIEW_MANAGE_POLLFORMS = "managePollForms";
    private static final String VIEW_CREATE_POLLFORM = "createPollForm";
    private static final String VIEW_MODIFY_POLLFORM = "modifyPollForm";
    private static final String VIEW_MODIFY_POLLFORM_QUESTION = "modifyPollFormQuestion";

    // Actions
    private static final String ACTION_CREATE_POLLFORM = "createPollForm";
    private static final String ACTION_MODIFY_POLLFORM = "modifyPollForm";
    private static final String ACTION_MODIFY_POLLFORM_QUESTION = "modifyPollFormQuestion";
    private static final String ACTION_REMOVE_POLLFORM = "removePollForm";
    private static final String ACTION_CONFIRM_REMOVE_POLLFORM = "confirmRemovePollForm";

    // Infos
    private static final String INFO_POLLFORM_CREATED = "poll.info.pollform.created";
    private static final String INFO_POLLFORM_UPDATED = "poll.info.pollform.updated";
    private static final String INFO_POLLFORM_QUESTION_UPDATED = "poll.info.pollform.question.updated";
    private static final String INFO_POLLFORM_REMOVED = "poll.info.pollform.removed";
    private static final String INFO_POLLFORM_QUESTION_REMOVED = "poll.info.pollform.question.removed";

    // Session variable to store working values
    private PollForm _pollform;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_POLLFORMS, defaultView = true )
    public String getManagePollForms( HttpServletRequest request )
    {
        _pollform = null;
        List<PollForm> listPollForms = PollFormHome.getPollFormsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_POLLFORM_LIST, listPollForms, JSP_MANAGE_POLLFORMS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_POLLFORMS, TEMPLATE_MANAGE_POLLFORMS, model );
    }

    /**
     * Returns the form to create a pollform
     *
     * @param request
     *            The Http request
     * @return the html code of the pollform form
     */
    @View( VIEW_CREATE_POLLFORM )
    public String getCreatePollForm( HttpServletRequest request )
    {
        _pollform = ( _pollform != null ) ? _pollform : new PollForm( );

        Map<String, Object> model = getModel( );
        List<Form> listForms = FormHome.getFormList( );
        model.put( MARK_FORM_LIST, listForms );
        model.put( MARK_POLLFORM, _pollform );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_POLLFORM ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_POLLFORM, TEMPLATE_CREATE_POLLFORM, model );
    }

    /**
     * Process the data capture form of a new pollform
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_POLLFORM )
    public String doCreatePollForm( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _pollform, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_POLLFORM ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _pollform, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_POLLFORM );
        }

        PollFormHome.create( _pollform );
        List<Question> questionList = QuestionHome.getListQuestionByIdForm( _pollform.getIdForm( ) );

        for ( Question question : questionList )
        {
            Entry entry = EntryHome.findByPrimaryKey( question.getEntry( ).getIdEntry( ) );
            question.setEntry( entry );
            PollFormQuestion newPollFormQuestion = new PollFormQuestion( );
            newPollFormQuestion.setIdQuestion( question.getId( ) );
            newPollFormQuestion.setIdForm( _pollform.getIdForm( ) );
            newPollFormQuestion.setIdPollForm( _pollform.getId( ) );
            PollFormQuestionHome.create( newPollFormQuestion );
        }

        addInfo( INFO_POLLFORM_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_POLLFORMS );
    }

    /**
     * Manages the removal form of a pollform whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_POLLFORM )
    public String getConfirmRemovePollForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_POLLFORM ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_POLLFORM ) );
        url.addParameter( PARAMETER_ID_POLLFORM, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_POLLFORM, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a pollform
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage pollforms
     */
    @Action( ACTION_REMOVE_POLLFORM )
    public String doRemovePollForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_POLLFORM ) );
        PollFormHome.remove( nId );
        addInfo( INFO_POLLFORM_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_POLLFORMS );
    }

    /**
     * Returns the form to update info about a pollform
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_POLLFORM )
    public String getModifyPollForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_POLLFORM ) );

        if ( _pollform == null || ( _pollform.getId( ) != nId ) )
        {
            _pollform = PollFormHome.findByPrimaryKey( nId );
        }

        int nIdFrom = _pollform.getIdForm( );
        LinkedHashMap<Step, List<Question>> stepWithQuestionList = new LinkedHashMap<>( );
        Form form = FormHome.findByPrimaryKey( nIdFrom );
        List<Question> questionList = QuestionHome.getListQuestionByIdForm( nIdFrom );
        List<Step> stepList = StepHome.getStepsListByForm( nIdFrom );

        for ( Question question : questionList )
        {
            Entry entry = EntryHome.findByPrimaryKey( question.getEntry( ).getIdEntry( ) );
            question.setEntry( entry );
        }

        for ( Step step : stepList )
        {
            List<Question> stepQuestionList = questionList.stream( ).filter( q -> q.getIdStep( ) == step.getId( ) ).collect( Collectors.toList( ) );
            stepWithQuestionList.put( step, stepQuestionList );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_POLL_FORM_QUESTION_LIST, PollFormQuestionHome.getPollFormQuestionListByFormId( _pollform.getId( ), nIdFrom ) );
        model.put( MARK_FORM, form );
        model.put( MARK_FORM_STEP_QUESTION_LIST, stepWithQuestionList );
        model.put( MARK_POLLFORM, _pollform );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_POLLFORM ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_POLLFORM, TEMPLATE_MODIFY_POLLFORM, model );
    }

    /**
     * Process the change form of a pollform
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_POLLFORM )
    public String doModifyPollForm( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _pollform, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_POLLFORM ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _pollform, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_POLLFORM, PARAMETER_ID_POLLFORM, _pollform.getId( ) );
        }

        List<Question> questionList = QuestionHome.getListQuestionByIdForm( _pollform.getIdForm( ) );
        for ( Question question : questionList )
        {
            int nIdQuestion = question.getId( );
            String checkBoxValue = request.getParameter( String.valueOf( question.getId( ) ) );
            PollFormQuestion pollFormQuestion = PollFormQuestionHome.findByQuestionId( _pollform.getId( ), nIdQuestion );
            if ( checkBoxValue != null )
            {
                pollFormQuestion.setIsChecked( true );
            }
            else
            {
                pollFormQuestion.setIsChecked( false );
            }
            PollFormQuestionHome.update( pollFormQuestion );
        }

        PollFormHome.update( _pollform );
        addInfo( INFO_POLLFORM_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_POLLFORMS );
    }

    /**
     * Returns the form to update info about a pollform
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_POLLFORM_QUESTION )
    public String getModifyPollFormQuestion( HttpServletRequest request )
    {
        int nIdPollFormQuestion = Integer.parseInt( request.getParameter( PARAMETER_ID_POLLFORM_QUESTION ) );
        PollFormQuestion pollFormQuestion = PollFormQuestionHome.findByPrimaryKey( nIdPollFormQuestion );
        Question question = QuestionHome.findByPrimaryKey( pollFormQuestion.getIdQuestion( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_POLL_FORM_QUESTION, pollFormQuestion );
        model.put( MARK_QUESTION, question );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_POLLFORM_QUESTION ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_POLLFORM_QUESTION, TEMPLATE_MODIFY_POLLFORM_QUESTION, model );
    }

    /**
     * Process the change form of a pollform
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_POLLFORM_QUESTION )
    public String doModifyPollFormQuestion( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_POLLFORM_QUESTION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        String idPollFormQuestion = request.getParameter( "id" );
        String type = request.getParameter( "type" );
        String isToolBox = request.getParameter( "is_visible_toolbox" );

        PollFormQuestion pollFormQuestion = PollFormQuestionHome.findByPrimaryKey( Integer.valueOf( idPollFormQuestion ) );
        pollFormQuestion.setType( type );
        if ( isToolBox != null )
        {
            pollFormQuestion.setIsToolBox( true );
        }
        else
        {
            pollFormQuestion.setIsToolBox( false );
        }
        PollFormQuestionHome.update( pollFormQuestion );
        addInfo( INFO_POLLFORM_QUESTION_UPDATED, getLocale( ) );

        return redirect( request, VIEW_MANAGE_POLLFORMS, "id", pollFormQuestion.getIdPollForm( ) );
    }

    /**
     * Process the change form of a pollform
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @View( VIEW_CHARTS )
    public String getCharts( HttpServletRequest request ) throws AccessDeniedException
    {
        Map<String, Object> model = getModel( );
        Map<PollVisualization, List<PollData>> pollVisualizationWithDataList = new HashMap<>( );

        String strIdPoll = request.getParameter( PARAMETER_ID_POLL );
        if ( strIdPoll != null )
        {
            PollForm pollForm = PollFormHome.findByPrimaryKey( Integer.valueOf( strIdPoll ) );
            List<PollFormQuestion> pollFormQuestionList = PollFormQuestionHome.getPollFormQuestionListByFormId( pollForm.getId( ), pollForm.getIdForm( ) );
            for ( PollFormQuestion pollFormQuestion : pollFormQuestionList )
            {
                if ( pollFormQuestion.getIsChecked( ) )
                {
                    Question question = QuestionHome.findByPrimaryKey( pollFormQuestion.getIdQuestion( ) );
                    PollVisualization pollVisualization = new PollVisualization( );
                    pollVisualization.setId( question.getId( ) );
                    pollVisualization.setTitle( question.getTitle( ) );
                    pollVisualization.setType( pollFormQuestion.getType( ) );
                    pollVisualization.setIsToolBox( pollFormQuestion.getIsToolbox( ) );
                    List<FormResponse> listFormResponses = FormResponseHome.selectAllFormResponsesUncompleteByIdForm( pollForm.getIdForm( ) );
                    List<String> responses = new ArrayList<String>( );
                    for ( FormResponse formResponse : listFormResponses )
                    {
                        if ( !formResponse.isFromSave( ) )
                        {
                            List<FormQuestionResponse> formQuestionResponseList = FormQuestionResponseHome
                                    .findFormQuestionResponseByResponseQuestion( formResponse.getId( ), pollFormQuestion.getIdQuestion( ) );
                            for ( FormQuestionResponse formQuestionResponse : formQuestionResponseList )
                            {
                                List<Response> responseList = formQuestionResponse.getEntryResponse( );
                                for ( Response response : responseList )
                                {
                                    if ( response.getField( ) != null )
                                    {
                                        responses.add( response.getResponseValue( ) );
                                    }
                                }
                            }
                        }
                    }

                    Map<String, Long> counted = responses.stream( ).collect( Collectors.groupingBy( Function.identity( ), Collectors.counting( ) ) );
                    List<PollData> listPollData = new ArrayList<>( );

                    counted.forEach( ( labelName, size ) -> {
                        listPollData.add( new PollData( labelName, (int) (long) size ) );
                    } );

                    pollVisualizationWithDataList.put( pollVisualization, listPollData );
                }

            }
            model.put( "poll_form", pollForm );
        }
        model.put( "poll_visualization_list", pollVisualizationWithDataList );
        return getPage( PROPERTY_PAGE_TITLE_VIEW_CHARTS, TEMPLATE_VIEW_CHARTS, model );
    }

}
