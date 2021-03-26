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

import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object PollForm
 */
public class PollFormBusinessTest extends LuteceTestCase
{
    private static final int IDFORM1 = 1;
    private static final int IDFORM2 = 2;
    private static final boolean ISVISIBLE1 = true;
    private static final boolean ISVISIBLE2 = false;

    /**
     * test PollForm
     */
    public void testBusiness( )
    {
        // Initialize an object
        PollForm pollForm = new PollForm( );
        pollForm.setIdForm( IDFORM1 );
        pollForm.setIsVisible( ISVISIBLE1 );

        // Create test
        PollFormHome.create( pollForm );
        PollForm pollFormStored = PollFormHome.findByPrimaryKey( pollForm.getId( ) );
        assertEquals( pollFormStored.getIdForm( ), pollForm.getIdForm( ) );
        assertEquals( pollFormStored.getIsVisible( ), pollForm.getIsVisible( ) );

        // Update test
        pollForm.setIdForm( IDFORM2 );
        pollForm.setIsVisible( ISVISIBLE2 );
        PollFormHome.update( pollForm );
        pollFormStored = PollFormHome.findByPrimaryKey( pollForm.getId( ) );
        assertEquals( pollFormStored.getIdForm( ), pollForm.getIdForm( ) );
        assertEquals( pollFormStored.getIsVisible( ), pollForm.getIsVisible( ) );

        // List test
        PollFormHome.getPollFormsList( );

        // Delete test
        PollFormHome.remove( pollForm.getId( ) );
        pollFormStored = PollFormHome.findByPrimaryKey( pollForm.getId( ) );
        assertNull( pollFormStored );

    }

}
