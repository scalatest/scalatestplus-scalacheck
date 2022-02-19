package org.scalatestplus.scalacheck

/**
 * Describes the scalacheck seed configuration to use when evaluating a property.
 *
 * @param viewFailingSeed Whether to display the seed for a property test that failed
 * @param specifyInitialSeed Whether to specify an initial seed
 */
case class ScalaCheckSeedConfiguration(viewFailingSeed: Boolean = true,
                                       specifyInitialSeed: Option[String] = None)
