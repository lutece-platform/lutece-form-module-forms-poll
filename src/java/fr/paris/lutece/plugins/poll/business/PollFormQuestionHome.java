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
package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for PollFormQuestion objects
 */
public final class PollFormQuestionHome
{
    // Static variable pointed at the DAO instance
    private static IPollFormQuestionDAO _dao = SpringContextService.getBean( "poll.pollFormQuestionDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "poll" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private PollFormQuestionHome( )
    {
    }

    /**
     * Create an instance of the pollFormQuestion class
     * 
     * @param pollFormQuestion
     *            The instance of the PollFormQuestion which contains the informations to store
     * @return The instance of pollFormQuestion which has been created with its primary key.
     */
    public static PollFormQuestion create( PollFormQuestion pollFormQuestion )
    {
        _dao.insert( pollFormQuestion, _plugin );

        return pollFormQuestion;
    }

    /**
     * Update of the pollFormQuestion which is specified in parameter
     * 
     * @param pollFormQuestion
     *            The instance of the PollFormQuestion which contains the data to store
     * @return The instance of the pollFormQuestion which has been updated
     */
    public static PollFormQuestion update( PollFormQuestion pollFormQuestion )
    {
        _dao.store( pollFormQuestion, _plugin );

        return pollFormQuestion;
    }

    /**
     * Remove the pollFormQuestion whose identifier is specified in parameter
     * 
     * @param nKey
     *            The pollFormQuestion Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a pollFormQuestion whose identifier is specified in parameter
     * 
     * @param nKey
     *            The pollFormQuestion primary key
     * @return an instance of PollFormQuestion
     */
    public static PollFormQuestion findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the pollFormQuestion objects and returns them as a list
     * 
     * @return the list which contains the data of all the pollFormQuestion objects
     */
    public static List<PollFormQuestion> getPollFormQuestionsList( )
    {
        return _dao.selectPollFormQuestionsList( _plugin );
    }

    /**
     * Load the id of all the pollFormQuestion objects and returns them as a list
     * 
     * @return the list which contains the id of all the pollFormQuestion objects
     */
    public static List<Integer> getIdPollFormQuestionsList( )
    {
        return _dao.selectIdPollFormQuestionsList( _plugin );
    }

    /**
     * Load the data of all the pollFormQuestion objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the pollFormQuestion objects
     */
    public static ReferenceList getPollFormQuestionsReferenceList( )
    {
        return _dao.selectPollFormQuestionsReferenceList( _plugin );
    }

    /**
     * Load the data of all the PollFormQuestion objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the PollFormQuestion objects
     */
    public static List<PollFormQuestion> getPollFormQuestionListByFormId( int nIdPollForm, int nFormId )
    {
        return _dao.selectPollFormQuestionListByFormId( nIdPollForm, nFormId, _plugin );
    }

    /**
     * Returns an instance of PollFormQuestion whose question identifier is specified in parameter
     * 
     * @param nKey
     *            The optionalQuestionIndexation primary key
     * @return an instance of OptionalQuestionIndexation
     */
    public static PollFormQuestion findByQuestionId( int nIdPollForm, int nQuestionId )
    {
        return _dao.loadByQuestionId( nIdPollForm, nQuestionId, _plugin );
    }
}
