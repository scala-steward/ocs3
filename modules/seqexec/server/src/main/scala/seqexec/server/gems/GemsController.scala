// Copyright (c) 2016-2019 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package seqexec.server.gems

import seqexec.server.tcs.Gaos.{PauseCondition, ResumeCondition}
import squants.Time

trait GemsController[F[_]] {
  import GemsController._

  def pause(reasons: Set[PauseCondition]): F[Unit]
  def resume(reasons: Set[ResumeCondition], config: GemsConfig): F[Unit]
  def observe(expTime: Time): F[Unit]
  def endObserve: F[Unit]

}

object GemsController {

  sealed trait GemsConfig

  case object GemsOff extends GemsConfig
  // TODO Complete ADT for GeMS configurations

}

