/*
 *  Copyright [2006-2007] [Stefan Kleine Stegemann]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbbs.blackboard.ks;

/**
 * A KnowledgeSource is an expert for a particular aspect of
 * a problem solving strategy. It reads entries from a Blackboard
 * and contributes new or updates existing entries.
 *
 * @author stefan
 */
public interface KnowledgeSource
{
   public boolean isEnabled(KSExecutionContext context);
   public void execute(KSExecutionContext context);
}
