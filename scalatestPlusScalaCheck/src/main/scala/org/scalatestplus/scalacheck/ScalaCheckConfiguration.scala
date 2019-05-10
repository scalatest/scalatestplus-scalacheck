/*
 * Copyright 2001-2018 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatestplus.scalacheck

import org.scalacheck.Test.Parameters
import org.scalacheck.Test.TestCallback
import org.scalatest.prop.Configuration

private[scalacheck] trait ScalaCheckConfiguration extends Configuration {

  private[scalacheck] def getScalaCheckParams(
                                              configParams: Seq[Configuration#PropertyCheckConfigParam],
                                              config: PropertyCheckConfiguration
                                            ): Parameters = {

    var minSuccessful: Option[Int] = None
    var maxDiscardedFactor: Option[Double] = None
    var pminSize: Option[Int] = None
    var psizeRange: Option[Int] = None
    var pworkers: Option[Int] = None

    var minSuccessfulTotalFound = 0
    var maxDiscardedFactorTotalFound = 0
    var minSizeTotalFound = 0
    var sizeRangeTotalFound = 0
    var workersTotalFound = 0

    for (configParam <- configParams) {
      configParam match {
        case param: MinSuccessful =>
          minSuccessful = Some(param.value)
          minSuccessfulTotalFound += 1
        case param: MaxDiscardedFactor =>
          maxDiscardedFactor = Some(param.value)
          maxDiscardedFactorTotalFound += 1
        case param: MinSize =>
          pminSize = Some(param.value)
          minSizeTotalFound += 1
        case param: SizeRange =>
          psizeRange = Some(param.value)
          sizeRangeTotalFound += 1
        case param: Workers =>
          pworkers = Some(param.value)
          workersTotalFound += 1
      }
    }

    if (minSuccessfulTotalFound > 1)
      throw new IllegalArgumentException("can pass at most one MinSuccessful config parameters, but " + minSuccessfulTotalFound + " were passed")
    if (maxDiscardedFactorTotalFound > 1)
      throw new IllegalArgumentException("can pass at most one MaxDiscardedFactor config parameters, but " + maxDiscardedFactorTotalFound + " were passed")
    if (minSizeTotalFound > 1)
      throw new IllegalArgumentException("can pass at most one MinSize config parameters, but " + minSizeTotalFound + " were passed")
    if (sizeRangeTotalFound > 1)
      throw new IllegalArgumentException("can pass at most one SizeRange config parameters, but " + sizeRangeTotalFound + " were passed")
    if (workersTotalFound > 1)
      throw new IllegalArgumentException("can pass at most one Workers config parameters, but " + workersTotalFound + " were passed")

    val minSuccessfulTests: Int = minSuccessful.getOrElse(config.minSuccessful)

    val minSize: Int = pminSize.getOrElse(config.minSize)

    val maxSize = psizeRange.getOrElse(config.sizeRange.value) + minSize

    val maxDiscardRatio: Float = maxDiscardedFactor.getOrElse(config.maxDiscardedFactor.value).toFloat

    Parameters.default
      .withMinSuccessfulTests(minSuccessfulTests)
      .withMinSize(minSize)
      .withMaxSize(maxSize)
      .withWorkers(pworkers.getOrElse(config.workers))
      .withTestCallback(new TestCallback {})
      .withMaxDiscardRatio(maxDiscardRatio)
      .withCustomClassLoader(None)
  }

}

private[scalacheck] object ScalaCheckConfiguration extends ScalaCheckConfiguration
