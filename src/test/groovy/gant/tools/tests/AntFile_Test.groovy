//  Gant -- A Groovy build framework based on scripting Ant tasks.
//
//  Copyright © 2008 Russel Winder
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package gant.tools.tests

import org.codehaus.gant.tests.GantTestCase

/**
 *  A test to ensure that the AntFile tool is not broken.
 *
 *  @author Russel Winder <russel.winder@concertant.com>
 */
final class AntFile_Test extends GantTestCase {

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////  createTempFile delivers a File object that delivers a string for the path that is platform specific.
  ////  Cannot use // to delimit the strings in the Gant script being created since / is the file separator
  ////  on most OSs.  Have to do something to avoid problems on Windows since '' strings still interpret \.
  ////  Fortunately Windows will accept / as the path separator, so transform all \ to / in all cases.
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private File temporaryFile
  private String temporaryFilePath
  void setUp ( ) {
    super.setUp ( )
    temporaryFile = File.createTempFile ( 'gant-antFile-' ,  '-executable' )
    temporaryFilePath = temporaryFile.path.replaceAll ( '\\\\' , '/' )
    temporaryFile.write ( '''
<project name="Gant Ant Use Test" default="execute">
  <target name="execute" description="Do something.">
    <echo message="Hello world."/>
  </target>
</project>
''' )
  }
  void tearDown ( ) {
    temporaryFile.delete ( )
    super.tearDown ( )
  }
  
  void testExecutable ( ) {
    script = """
includeTool << gant.tools.AntFile
AntFile.includeTargets ( '${temporaryFilePath}' )
setDefaultTarget ( 'execute' )
"""
    //assertEquals ( 0 , processTargets ( ) )
    System.err.println ( processTargets ( ) )
    assertEquals ( '''     [echo] Hello world.
''' , output )
  }
  void testListing ( ) {
    script = """
includeTool << gant.tools.AntFile
AntFile.includeTargets ( '${temporaryFilePath}' )
setDefaultTarget ( 'execute' )
"""
    //assertEquals ( 0 , gant.processArgs ( [ '-p' , '-f' , '-' ] as String[] ) )
    System.err.println ( gant.processArgs ( [ '-p' , '-f' , '-' ] as String[] ) )
    assertEquals ( '''
 execute  Do something.

Default target is execute.

''' , output )
  }
}
