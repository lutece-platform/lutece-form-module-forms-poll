package fr.paris.lutece.plugins.poll.service;

import fr.paris.lutece.plugins.poll.business.PollForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.poll.business.PollData;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.plugins.poll.business.PollFormQuestion;
import fr.paris.lutece.plugins.poll.business.PollFormQuestionHome;
import fr.paris.lutece.plugins.poll.business.PollVisualization;

public class PollFormService
{

    public static Map<PollVisualization, List<PollData>> getPollVisualizationList( int nIdPoll )
    {

        Map<PollVisualization, List<PollData>> pollVisualizationWithDataList = new HashMap<>( );

        PollForm pollForm = PollFormHome.findByPrimaryKey( Integer.valueOf( nIdPoll ) );
        List<PollFormQuestion> pollFormQuestionList = PollFormQuestionHome.getPollFormQuestionListByFormId( pollForm.getId( ), pollForm.getIdForm( ) );
        List<FormResponse> listFormResponses = FormResponseHome.selectAllFormResponsesUncompleteByIdForm( pollForm.getIdForm( ) );
        List<FormQuestionResponse> listFormQuestionResponse = FormQuestionResponseHome.getFormQuestionResponseListByFormResponseList(
                listFormResponses.parallelStream( ).map( i -> i.getId( ) ).distinct( ).collect( Collectors.toList( ) ) );

        List<PollFormQuestion> pollFormQuestionListChecked = pollFormQuestionList.stream( )
                .filter( formQuestionResponse -> formQuestionResponse.getIsChecked( ) == true ).collect( Collectors.toList( ) );

        List<Integer> listPollFormQuestionId = pollFormQuestionListChecked.stream( ).map( PollFormQuestion::getIdQuestion ).collect( Collectors.toList( ) );

        List<Question> listQuestions = QuestionHome.findByPrimaryKeyList( listPollFormQuestionId );

        for ( PollFormQuestion pollFormQuestion : pollFormQuestionListChecked )
        {

            Question question = listQuestions.stream( ).filter( x -> x.getId( ) == pollFormQuestion.getIdQuestion( ) ).findFirst( ).orElse( null );

            PollVisualization pollVisualization = new PollVisualization( );
            pollVisualization.setId( question.getId( ) );
            pollVisualization.setTitle( question.getTitle( ) );
            pollVisualization.setType( pollFormQuestion.getType( ) );
            pollVisualization.setIsToolBox( pollFormQuestion.getIsToolbox( ) );
            List<String> responses = new ArrayList<String>( );

            List<FormQuestionResponse> formQuestionResponseList = listFormQuestionResponse.parallelStream( )
                    .filter( x -> x.getQuestion( ).getId( ) == question.getId( ) ).collect( Collectors.toList( ) );

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

            Map<String, Long> counted = responses.stream( ).collect( Collectors.groupingBy( Function.identity( ), Collectors.counting( ) ) );
            List<PollData> listPollData = new ArrayList<>( );

            counted.forEach( ( labelName, size ) -> {
                listPollData.add( new PollData( labelName, (int) (long) size ) );
            } );
            pollVisualizationWithDataList.put( pollVisualization, listPollData );
        }

        return pollVisualizationWithDataList;
    }

}
