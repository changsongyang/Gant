//  Gant -- A Groovy build framework based on scripting Ant tasks.
//
//  Copyright © 2007-8 Russel Winder
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
 *  A test to ensure that the Execute tool is not broken.
 *
 *  @author Russel Winder <russel.winder@concertant.com>
 */
final class Execute_Test extends GantTestCase {
  void testExecutableString ( ) {
    script = '''includeTool << gant.tools.Execute
target ( testing : '' ) { execute.executable ( 'echo 1' ) }
'''
    assertEquals ( 0 , processTargets ( 'testing' ) )
    assertEquals ( '''  [execute] echo 1
1
''' , output )
  }
  void testExecutableListOfString ( ) {
    script = '''includeTool << gant.tools.Execute
target ( testing : '' ) { execute.executable ( [ 'echo' , '1' ] ) }
'''
    assertEquals ( 0 , processTargets ( 'testing' ) )
    assertEquals (  ( groovyMinorVersion > 5 ) ? '''  [execute] [echo, 1]
1
''' : '''  [execute] ["echo", "1"]
1
''' , output ) 
  }
  void testShellString ( ) {
    script = '''includeTool << gant.tools.Execute
target ( testing : '' ) { execute.shell ( 'echo 1' ) }
''' 
    assertEquals ( 0 , processTargets ( 'testing' ) )
    assertEquals ( '''    [shell] echo 1
1
''' , output ) 
  }
  void testExecuteReturnCodeCorrect ( ) {
    script = '''includeTool << gant.tools.Execute
target ( testing : '' ) { assert execute.executable ( 'echo 1' ) == 0 }
'''
    assertEquals ( 0 , processTargets ( 'testing' ) )
    assertEquals ( '''  [execute] echo 1
1
''' , output )
  }
  void testExecuteReturnCodeError ( ) {
    script = '''includeTool << gant.tools.Execute
target ( testing : '' ) { assert execute.executable ( 'false' ) == 1 }
'''
    assertEquals ( 0 , processTargets ( 'testing' ) )
    assertEquals ( '''  [execute] false
''' , output )
  }
}
