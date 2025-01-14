CREATE TABLE `collezionabile` (
  `id` int(11) NOT NULL,
  `gioco` varchar(255) NOT NULL,
  `posMappa` int(11) DEFAULT NULL,
  `numsalvataggio` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `valore` int(11) NOT NULL
) ;

CREATE TABLE `collocazione` (
  `id` int(11) NOT NULL,
  `mappa_id` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `sprite_id` int(11) NOT NULL,
  `xWaypoint1` int(11) DEFAULT '0',
  `xWaypoint2` int(11) DEFAULT '0',
  `associato` int(11) DEFAULT NULL
) ;

INSERT INTO `collocazione` (`id`, `mappa_id`, `x`, `y`, `sprite_id`, `xWaypoint1`, `xWaypoint2`, `associato`) VALUES
(433, 11, 0, 600, 20, 0, 0, NULL),
(434, 11, 100, 600, 20, 0, 0, NULL),
(435, 11, 200, 600, 20, 0, 0, NULL),
(436, 11, 300, 600, 20, 0, 0, NULL),
(437, 11, 400, 600, 20, 0, 0, NULL),
(438, 11, 500, 600, 20, 0, 0, NULL),
(439, 11, 600, 600, 20, 0, 0, NULL),
(440, 11, 700, 600, 20, 0, 0, NULL),
(441, 11, 800, 600, 20, 0, 0, NULL),
(442, 11, 900, 600, 20, 0, 0, NULL),
(443, 11, 1000, 600, 20, 0, 0, NULL),
(448, 11, 1300, 600, 20, 0, 0, NULL),
(449, 11, 1300, 700, 20, 0, 0, NULL),
(450, 11, 1300, 800, 20, 0, 0, NULL),
(451, 11, 1300, 900, 20, 0, 0, NULL),
(452, 11, 1300, 1000, 20, 0, 0, NULL),
(453, 11, 1200, 1000, 20, 0, 0, NULL),
(454, 11, 1100, 1000, 20, 0, 0, NULL),
(455, 11, 1100, 1000, 20, 0, 0, NULL),
(456, 11, 1100, 1000, 20, 0, 0, NULL),
(457, 11, 1000, 1000, 20, 0, 0, NULL),
(458, 11, 900, 1000, 20, 0, 0, NULL),
(459, 11, 800, 1000, 20, 0, 0, NULL),
(460, 11, 700, 1000, 20, 0, 0, NULL),
(461, 11, 600, 1000, 20, 0, 0, NULL),
(462, 11, 500, 1000, 20, 0, 0, NULL),
(463, 11, 400, 1000, 20, 0, 0, NULL),
(464, 11, 300, 1000, 20, 0, 0, NULL),
(465, 11, 200, 1000, 20, 0, 0, NULL),
(466, 11, 100, 1000, 20, 0, 0, NULL),
(469, 11, 0, 1000, 20, 0, 0, NULL),
(474, 11, 0, 900, 20, 0, 0, NULL),
(475, 11, 0, 800, 20, 0, 0, NULL),
(476, 11, 0, 700, 20, 0, 0, NULL),
(477, 11, 1300, 500, 20, 0, 0, NULL),
(478, 11, 1300, 400, 20, 0, 0, NULL),
(479, 11, 1300, 300, 20, 0, 0, NULL),
(480, 11, 1300, 200, 20, 0, 0, NULL),
(481, 11, 1300, 100, 20, 0, 0, NULL),
(482, 11, 1300, 0, 20, 0, 0, NULL),
(483, 11, 100, 900, 7, 0, 0, 606),
(484, 11, 600, 900, 7, 0, 0, 656),
(486, 11, 0, 1400, 20, 0, 0, NULL),
(487, 11, 0, 1500, 20, 0, 0, NULL),
(488, 11, 0, 1600, 20, 0, 0, NULL),
(489, 11, 100, 1600, 20, 0, 0, NULL),
(490, 11, 200, 1600, 20, 0, 0, NULL),
(491, 11, 300, 1600, 20, 0, 0, NULL),
(492, 11, 0, 1300, 20, 0, 0, NULL),
(493, 11, 0, 1200, 20, 0, 0, NULL),
(494, 11, 0, 1100, 20, 0, 0, NULL),
(496, 11, 400, 1600, 20, 0, 0, NULL),
(497, 11, 500, 1600, 20, 0, 0, NULL),
(498, 11, 500, 1500, 20, 0, 0, NULL),
(499, 11, 500, 1400, 20, 0, 0, NULL),
(500, 11, 500, 1300, 20, 0, 0, NULL),
(501, 11, 500, 1200, 20, 0, 0, NULL),
(502, 11, 500, 1100, 20, 0, 0, NULL),
(503, 11, 400, 1500, 7, 0, 0, 524),
(504, 11, 200, 1500, 7, 0, 0, 608),
(505, 11, 300, 1500, 7, 0, 0, 713),
(506, 11, 600, 1600, 20, 0, 0, NULL),
(507, 11, 700, 1600, 20, 0, 0, NULL),
(508, 11, 800, 1600, 20, 0, 0, NULL),
(509, 11, 900, 1600, 20, 0, 0, NULL),
(510, 11, 900, 1500, 20, 0, 0, NULL),
(511, 11, 900, 1400, 20, 0, 0, NULL),
(512, 11, 900, 1300, 20, 0, 0, NULL),
(513, 11, 900, 1100, 20, 0, 0, NULL),
(514, 11, 900, 1200, 20, 0, 0, NULL),
(515, 11, 800, 1500, 7, 0, 0, 656),
(516, 11, 1200, 1100, 20, 0, 0, NULL),
(517, 11, 1200, 1300, 20, 0, 0, NULL),
(518, 11, 1200, 1200, 20, 0, 0, NULL),
(519, 11, 1200, 1400, 20, 0, 0, NULL),
(520, 11, 1200, 1500, 20, 0, 0, NULL),
(521, 11, 1200, 1600, 20, 0, 0, NULL),
(522, 11, 1100, 1600, 20, 0, 0, NULL),
(523, 11, 1000, 1600, 20, 0, 0, NULL),
(524, 11, 600, 1500, 7, 0, 0, 503),
(526, 11, 1400, 1000, 20, 0, 0, NULL),
(527, 11, 1500, 1000, 20, 0, 0, NULL),
(528, 11, 1600, 1000, 20, 0, 0, NULL),
(529, 11, 1700, 1000, 20, 0, 0, NULL),
(530, 11, 1900, 1000, 20, 0, 0, NULL),
(531, 11, 1800, 1000, 20, 0, 0, NULL),
(532, 11, 2000, 1000, 20, 0, 0, NULL),
(533, 11, 2100, 1000, 20, 0, 0, NULL),
(534, 11, 2300, 1000, 20, 0, 0, NULL),
(535, 11, 2200, 1000, 20, 0, 0, NULL),
(536, 11, 2400, 1000, 20, 0, 0, NULL),
(537, 11, 2600, 1000, 20, 0, 0, NULL),
(538, 11, 2500, 1000, 20, 0, 0, NULL),
(539, 11, 2700, 1000, 20, 0, 0, NULL),
(540, 11, 2800, 1000, 20, 0, 0, NULL),
(541, 11, 3000, 1000, 20, 0, 0, NULL),
(542, 11, 2900, 1000, 20, 0, 0, NULL),
(543, 11, 3100, 1000, 20, 0, 0, NULL),
(544, 11, 1400, 300, 20, 0, 0, NULL),
(545, 11, 1500, 300, 20, 0, 0, NULL),
(546, 11, 1800, 0, 20, 0, 0, NULL),
(547, 11, 1800, 100, 20, 0, 0, NULL),
(548, 11, 1800, 300, 20, 0, 0, NULL),
(549, 11, 1800, 200, 20, 0, 0, NULL),
(550, 11, 1800, 400, 20, 0, 0, NULL),
(551, 11, 1800, 500, 20, 0, 0, NULL),
(552, 11, 1800, 600, 20, 0, 0, NULL),
(553, 11, 1800, 700, 20, 0, 0, NULL),
(554, 11, 1800, 800, 20, 0, 0, NULL),
(555, 11, 1800, 900, 20, 0, 0, NULL),
(556, 11, 1400, 200, 7, 0, 0, 583),
(557, 11, 1400, 900, 7, 0, 0, 584),
(558, 11, 1300, 1500, 7, 0, 0, 764),
(559, 11, 1300, 1600, 20, 0, 0, NULL),
(560, 11, 1400, 1600, 20, 0, 0, NULL),
(561, 11, 1500, 1600, 20, 0, 0, NULL),
(562, 11, 1400, 1200, 2, 1300, 1700, NULL),
(563, 11, 1500, 1500, 20, 0, 0, NULL),
(564, 11, 1600, 1600, 20, 0, 0, NULL),
(565, 11, 1600, 1500, 20, 0, 0, NULL),
(566, 11, 1600, 1400, 20, 0, 0, NULL),
(568, 11, 1700, 1300, 20, 0, 0, NULL),
(569, 11, 1700, 1400, 20, 0, 0, NULL),
(570, 11, 1700, 1500, 20, 0, 0, NULL),
(571, 11, 1700, 1600, 20, 0, 0, NULL),
(572, 11, 1800, 1200, 20, 0, 0, NULL),
(573, 11, 1800, 1300, 20, 0, 0, NULL),
(574, 11, 1800, 1400, 20, 0, 0, NULL),
(575, 11, 1800, 1500, 20, 0, 0, NULL),
(576, 11, 1800, 1600, 20, 0, 0, NULL),
(577, 11, 1900, 1100, 20, 0, 0, NULL),
(578, 11, 1900, 1300, 20, 0, 0, NULL),
(579, 11, 1900, 1200, 20, 0, 0, NULL),
(580, 11, 1900, 1400, 20, 0, 0, NULL),
(581, 11, 1900, 1500, 20, 0, 0, NULL),
(582, 11, 1900, 1600, 20, 0, 0, NULL),
(583, 11, 1800, 1100, 7, 0, 0, 556),
(584, 11, 2000, 1100, 7, 0, 0, 1096),
(585, 11, 2000, 1200, 20, 0, 0, NULL),
(586, 11, 2000, 1300, 20, 0, 0, NULL),
(587, 11, 2000, 1400, 20, 0, 0, NULL),
(588, 11, 2000, 1500, 20, 0, 0, NULL),
(589, 11, 2000, 1600, 20, 0, 0, NULL),
(590, 11, 2100, 1300, 20, 0, 0, NULL),
(591, 11, 2200, 1300, 20, 0, 0, NULL),
(592, 11, 2200, 1400, 20, 0, 0, NULL),
(593, 11, 2200, 1500, 20, 0, 0, NULL),
(594, 11, 2100, 1400, 20, 0, 0, NULL),
(595, 11, 2100, 1500, 20, 0, 0, NULL),
(596, 11, 2100, 1600, 20, 0, 0, NULL),
(597, 11, 2200, 1600, 20, 0, 0, NULL),
(598, 11, 2300, 1600, 20, 0, 0, NULL),
(599, 11, 2400, 1600, 20, 0, 0, NULL),
(600, 11, 2400, 1500, 20, 0, 0, NULL),
(601, 11, 2400, 1400, 20, 0, 0, NULL),
(602, 11, 2400, 1300, 20, 0, 0, NULL),
(603, 11, 2400, 1200, 20, 0, 0, NULL),
(604, 11, 2400, 1100, 20, 0, 0, NULL),
(605, 11, 2300, 1500, 7, 0, 0, 803),
(606, 11, 100, 1700, 7, 0, 0, 483),
(608, 11, 100, 2800, 7, 0, 0, 504),
(609, 11, 0, 1700, 20, 0, 0, NULL),
(610, 11, 0, 1800, 20, 0, 0, NULL),
(611, 11, 0, 1900, 20, 0, 0, NULL),
(612, 11, 0, 2000, 20, 0, 0, NULL),
(613, 11, 0, 2100, 20, 0, 0, NULL),
(614, 11, 0, 2200, 20, 0, 0, NULL),
(615, 11, 0, 2300, 20, 0, 0, NULL),
(616, 11, 0, 2400, 20, 0, 0, NULL),
(617, 11, 0, 2500, 20, 0, 0, NULL),
(618, 11, 0, 2600, 20, 0, 0, NULL),
(619, 11, 0, 2700, 20, 0, 0, NULL),
(620, 11, 0, 2800, 20, 0, 0, NULL),
(621, 11, 0, 2900, 20, 0, 0, NULL),
(622, 11, 100, 2900, 20, 0, 0, NULL),
(623, 11, 200, 2900, 20, 0, 0, NULL),
(624, 11, 200, 2800, 20, 0, 0, NULL),
(625, 11, 200, 2700, 20, 0, 0, NULL),
(626, 11, 200, 2600, 20, 0, 0, NULL),
(627, 11, 200, 2500, 20, 0, 0, NULL),
(628, 11, 200, 2400, 20, 0, 0, NULL),
(629, 11, 200, 2300, 20, 0, 0, NULL),
(630, 11, 200, 2200, 20, 0, 0, NULL),
(631, 11, 200, 2100, 20, 0, 0, NULL),
(632, 11, 200, 2000, 20, 0, 0, NULL),
(633, 11, 200, 1900, 20, 0, 0, NULL),
(634, 11, 200, 1800, 20, 0, 0, NULL),
(635, 11, 200, 1700, 20, 0, 0, NULL),
(636, 11, 300, 2000, 20, 0, 0, NULL),
(637, 11, 300, 1900, 20, 0, 0, NULL),
(638, 11, 300, 1800, 20, 0, 0, NULL),
(639, 11, 300, 1700, 20, 0, 0, NULL),
(640, 11, 400, 1700, 20, 0, 0, NULL),
(641, 11, 500, 1700, 20, 0, 0, NULL),
(642, 11, 600, 1700, 20, 0, 0, NULL),
(643, 11, 700, 1700, 20, 0, 0, NULL),
(644, 11, 300, 2100, 20, 0, 0, NULL),
(645, 11, 300, 2200, 20, 0, 0, NULL),
(646, 11, 300, 2300, 20, 0, 0, NULL),
(647, 11, 300, 2400, 20, 0, 0, NULL),
(648, 11, 300, 2500, 20, 0, 0, NULL),
(649, 11, 300, 2600, 20, 0, 0, NULL),
(650, 11, 300, 2700, 20, 0, 0, NULL),
(651, 11, 300, 2800, 20, 0, 0, NULL),
(652, 11, 300, 2900, 20, 0, 0, NULL),
(653, 11, 1513, 532, 2, 1400, 1700, NULL),
(654, 11, 800, 1700, 20, 0, 0, NULL),
(656, 11, 718, 1814, 7, 0, 0, 515),
(657, 11, 400, 2500, 7, 0, 0, 761),
(658, 11, 900, 2500, 7, 0, 0, 706),
(659, 11, 400, 2900, 20, 0, 0, NULL),
(660, 11, 500, 2900, 20, 0, 0, NULL),
(661, 11, 600, 2900, 20, 0, 0, NULL),
(662, 11, 700, 2900, 20, 0, 0, NULL),
(663, 11, 800, 2900, 20, 0, 0, NULL),
(664, 11, 900, 2900, 20, 0, 0, NULL),
(665, 11, 1000, 2900, 20, 0, 0, NULL),
(666, 11, 1000, 2800, 20, 0, 0, NULL),
(667, 11, 1000, 2700, 20, 0, 0, NULL),
(668, 11, 1000, 2600, 20, 0, 0, NULL),
(669, 11, 1000, 2500, 20, 0, 0, NULL),
(670, 11, 1000, 2400, 20, 0, 0, NULL),
(671, 11, 1000, 2300, 20, 0, 0, NULL),
(672, 11, 1000, 2200, 20, 0, 0, NULL),
(673, 11, 1000, 2100, 20, 0, 0, NULL),
(674, 11, 1000, 2000, 20, 0, 0, NULL),
(675, 11, 1000, 1900, 20, 0, 0, NULL),
(676, 11, 1000, 1800, 20, 0, 0, NULL),
(677, 11, 1000, 1700, 20, 0, 0, NULL),
(678, 11, 900, 1700, 20, 0, 0, NULL),
(679, 11, 1100, 1700, 20, 0, 0, NULL),
(680, 11, 1200, 1700, 20, 0, 0, NULL),
(681, 11, 1300, 1700, 20, 0, 0, NULL),
(682, 11, 1400, 1700, 20, 0, 0, NULL),
(683, 11, 1500, 1700, 20, 0, 0, NULL),
(684, 11, 1600, 1700, 20, 0, 0, NULL),
(685, 11, 1700, 1700, 20, 0, 0, NULL),
(686, 11, 1800, 1700, 20, 0, 0, NULL),
(687, 11, 1900, 1700, 20, 0, 0, NULL),
(688, 11, 2100, 1700, 20, 0, 0, NULL),
(689, 11, 2000, 1700, 20, 0, 0, NULL),
(690, 11, 2200, 1700, 20, 0, 0, NULL),
(691, 11, 2300, 1700, 20, 0, 0, NULL),
(692, 11, 2400, 1700, 20, 0, 0, NULL),
(693, 11, 1100, 1800, 20, 0, 0, NULL),
(694, 11, 1200, 1800, 20, 0, 0, NULL),
(695, 11, 1415, 1915, 5, 0, 0, NULL),
(696, 11, 1300, 1800, 20, 0, 0, NULL),
(697, 11, 1400, 1800, 20, 0, 0, NULL),
(698, 11, 1500, 1800, 20, 0, 0, NULL),
(699, 11, 1500, 1900, 20, 0, 0, NULL),
(700, 11, 1500, 2000, 20, 0, 0, NULL),
(701, 11, 1400, 2000, 20, 0, 0, NULL),
(702, 11, 1300, 2000, 20, 0, 0, NULL),
(703, 11, 1200, 2000, 20, 0, 0, NULL),
(704, 11, 1100, 2000, 20, 0, 0, NULL),
(705, 11, 1100, 1900, 7, 0, 0, 807),
(706, 11, 1100, 2100, 7, 0, 0, 658),
(707, 11, 1100, 2200, 20, 0, 0, NULL),
(708, 11, 1200, 2200, 20, 0, 0, NULL),
(709, 11, 1300, 2200, 20, 0, 0, NULL),
(710, 11, 1400, 2200, 20, 0, 0, NULL),
(711, 11, 1500, 2200, 20, 0, 0, NULL),
(712, 11, 1500, 2100, 20, 0, 0, NULL),
(713, 11, 700, 2800, 7, 0, 0, 505),
(714, 11, 1415, 2115, 9, 0, 0, NULL),
(715, 11, 1100, 2400, 20, 0, 0, NULL),
(716, 11, 1200, 2400, 20, 0, 0, NULL),
(717, 11, 1300, 2400, 20, 0, 0, NULL),
(718, 11, 1500, 2400, 20, 0, 0, NULL),
(719, 11, 1400, 2400, 20, 0, 0, NULL),
(720, 11, 1500, 2300, 20, 0, 0, NULL),
(721, 11, 1100, 2300, 7, 0, 0, 808),
(722, 11, 1313, 2332, 2, 1100, 1400, NULL),
(723, 11, 1500, 2600, 20, 0, 0, NULL),
(724, 11, 1400, 2600, 20, 0, 0, NULL),
(725, 11, 1300, 2600, 20, 0, 0, NULL),
(726, 11, 1200, 2600, 20, 0, 0, NULL),
(727, 11, 1100, 2600, 20, 0, 0, NULL),
(728, 11, 1500, 2500, 20, 0, 0, NULL),
(729, 11, 1500, 2700, 20, 0, 0, NULL),
(730, 11, 1500, 2800, 20, 0, 0, NULL),
(731, 11, 1500, 2900, 20, 0, 0, NULL),
(732, 11, 1400, 2900, 20, 0, 0, NULL),
(733, 11, 1300, 2900, 20, 0, 0, NULL),
(734, 11, 1200, 2900, 20, 0, 0, NULL),
(735, 11, 1100, 2900, 20, 0, 0, NULL),
(736, 11, 1100, 2800, 7, 0, 0, 811),
(737, 11, 1313, 2832, 2, 1100, 1400, NULL),
(739, 11, 1300, 2700, 6, 0, 0, NULL),
(740, 11, 1200, 2700, 6, 0, 0, NULL),
(741, 11, 1100, 2700, 6, 0, 0, NULL),
(742, 11, 1100, 2500, 7, 0, 0, 812),
(744, 11, 1300, 2500, 6, 0, 0, NULL),
(745, 11, 1200, 2500, 6, 0, 0, NULL),
(746, 11, 1800, 1800, 20, 0, 0, NULL),
(747, 11, 1800, 1900, 20, 0, 0, NULL),
(748, 11, 1800, 2000, 20, 0, 0, NULL),
(749, 11, 1800, 2100, 20, 0, 0, NULL),
(750, 11, 1800, 2200, 20, 0, 0, NULL),
(751, 11, 1800, 2300, 20, 0, 0, NULL),
(752, 11, 1800, 2400, 20, 0, 0, NULL),
(753, 11, 1800, 2500, 20, 0, 0, NULL),
(754, 11, 1800, 2600, 20, 0, 0, NULL),
(755, 11, 1800, 2700, 20, 0, 0, NULL),
(756, 11, 1800, 2800, 20, 0, 0, NULL),
(757, 11, 1700, 2900, 20, 0, 0, NULL),
(758, 11, 1800, 2900, 20, 0, 0, NULL),
(760, 11, 1600, 2900, 20, 0, 0, NULL),
(761, 11, 1700, 1800, 7, 0, 0, 657),
(765, 11, 1700, 2800, 7, 0, 0, 806),
(769, 11, 1900, 2000, 20, 0, 0, NULL),
(770, 11, 2000, 2000, 20, 0, 0, NULL),
(771, 11, 2100, 2000, 20, 0, 0, NULL),
(772, 11, 2200, 2000, 20, 0, 0, NULL),
(773, 11, 2300, 2000, 20, 0, 0, NULL),
(774, 11, 2400, 2000, 20, 0, 0, NULL),
(775, 11, 2400, 1900, 20, 0, 0, NULL),
(776, 11, 2400, 1800, 20, 0, 0, NULL),
(777, 11, 3100, 900, 20, 0, 0, NULL),
(778, 11, 3100, 800, 20, 0, 0, NULL),
(779, 11, 3100, 700, 20, 0, 0, NULL),
(780, 11, 3100, 600, 20, 0, 0, NULL),
(781, 11, 3100, 500, 20, 0, 0, NULL),
(782, 11, 3100, 400, 20, 0, 0, NULL),
(783, 11, 3100, 300, 20, 0, 0, NULL),
(784, 11, 3100, 200, 20, 0, 0, NULL),
(785, 11, 3000, 200, 20, 0, 0, NULL),
(786, 11, 2900, 200, 20, 0, 0, NULL),
(787, 11, 2800, 200, 20, 0, 0, NULL),
(788, 11, 2700, 200, 20, 0, 0, NULL),
(789, 11, 2600, 200, 20, 0, 0, NULL),
(790, 11, 2500, 200, 20, 0, 0, NULL),
(791, 11, 2300, 200, 20, 0, 0, NULL),
(792, 11, 2400, 200, 20, 0, 0, NULL),
(793, 11, 2200, 200, 20, 0, 0, NULL),
(794, 11, 2100, 200, 20, 0, 0, NULL),
(795, 11, 2000, 200, 20, 0, 0, NULL),
(796, 11, 1900, 200, 20, 0, 0, NULL),
(797, 11, 2925, 814, 4, 1900, 3000, NULL),
(798, 11, 2025, 814, 4, 1900, 3000, NULL),
(799, 11, 2425, 814, 4, 1900, 3000, NULL),
(800, 11, 2813, 932, 2, 2000, 2900, NULL),
(801, 11, 2213, 932, 2, 2000, 2900, NULL),
(802, 11, 2613, 932, 2, 2000, 2900, NULL),
(803, 11, 1900, 900, 7, 0, 0, 605),
(806, 11, 1200, 900, 7, 0, 0, 765),
(807, 11, 1600, 2300, 7, 0, 0, 705),
(808, 11, 1600, 2800, 7, 0, 0, 721),
(809, 11, 2200, 1200, 7, 0, 0, 810),
(810, 11, 1900, 1900, 7, 0, 0, 809),
(811, 11, 2100, 1900, 7, 0, 0, 736),
(812, 11, 2300, 1900, 7, 0, 0, 742),
(813, 11, 0, 500, 20, 0, 0, NULL),
(814, 11, 0, 400, 20, 0, 0, NULL),
(815, 11, 0, 300, 20, 0, 0, NULL),
(816, 11, 0, 200, 20, 0, 0, NULL),
(817, 11, 0, 100, 20, 0, 0, NULL),
(818, 11, 0, 0, 20, 0, 0, NULL),
(819, 11, 200, 0, 20, 0, 0, NULL),
(820, 11, 100, 0, 20, 0, 0, NULL),
(821, 11, 300, 0, 20, 0, 0, NULL),
(822, 11, 400, 0, 20, 0, 0, NULL),
(823, 11, 500, 0, 20, 0, 0, NULL),
(824, 11, 600, 0, 20, 0, 0, NULL),
(825, 11, 700, 0, 20, 0, 0, NULL),
(826, 11, 800, 0, 20, 0, 0, NULL),
(827, 11, 900, 0, 20, 0, 0, NULL),
(828, 11, 1000, 0, 20, 0, 0, NULL),
(829, 11, 1100, 0, 20, 0, 0, NULL),
(830, 11, 1200, 0, 20, 0, 0, NULL),
(831, 11, 1400, 0, 20, 0, 0, NULL),
(832, 11, 1500, 0, 20, 0, 0, NULL),
(833, 11, 1600, 0, 20, 0, 0, NULL),
(834, 11, 1500, 0, 20, 0, 0, NULL),
(835, 11, 1700, 0, 20, 0, 0, NULL),
(1096, 11, 1700, 2300, 7, 0, 0, 584),
(1097, 1, 0, 700, 3, 0, 0, NULL),
(1098, 1, 200, 700, 3, 0, 0, NULL),
(1099, 1, 100, 700, 3, 0, 0, NULL),
(1100, 1, 0, 700, 3, 0, 0, NULL),
(1101, 1, 300, 700, 3, 0, 0, NULL),
(1102, 1, 400, 700, 3, 0, 0, NULL),
(1103, 1, 500, 700, 3, 0, 0, NULL),
(1104, 1, 600, 700, 3, 0, 0, NULL),
(1105, 1, 700, 700, 3, 0, 0, NULL),
(1106, 1, 800, 700, 3, 0, 0, NULL),
(1107, 1, 900, 700, 3, 0, 0, NULL),
(1108, 1, 1000, 700, 3, 0, 0, NULL),
(1109, 1, 400, 600, 3, 0, 0, NULL),
(1110, 1, 500, 500, 3, 0, 0, NULL),
(1111, 1, 600, 500, 3, 0, 0, NULL),
(1112, 1, 600, 400, 3, 0, 0, NULL),
(1113, 1, 500, 600, 3, 0, 0, NULL),
(1114, 1, 600, 600, 3, 0, 0, NULL),
(1115, 1, 500, 600, 19, 0, 0, NULL),
(1116, 1, 600, 500, 19, 0, 0, NULL),
(1117, 1, 600, 600, 19, 0, 0, NULL),
(1118, 1, 500, 600, 19, 0, 0, NULL),
(1119, 1, 400, 700, 19, 0, 0, NULL),
(1120, 1, 500, 700, 19, 0, 0, NULL),
(1121, 1, 600, 500, 19, 0, 0, NULL),
(1122, 1, 600, 700, 19, 0, 0, NULL),
(1123, 1, 0, 600, 19, 0, 0, NULL),
(1124, 1, 0, 500, 19, 0, 0, NULL),
(1125, 1, 0, 400, 19, 0, 0, NULL),
(1126, 1, 0, 300, 19, 0, 0, NULL),
(1127, 1, 0, 200, 19, 0, 0, NULL),
(1129, 1, 0, 100, 3, 0, 0, NULL),
(1130, 1, 0, 700, 19, 0, 0, NULL),
(1131, 1, 815, 615, 6, 0, 0, NULL),
(1132, 1, 915, 615, 6, 0, 0, NULL),
(1133, 1, 1015, 615, 6, 0, 0, NULL),
(1134, 1, 915, 615, 6, 0, 0, NULL),
(1135, 1, 815, 615, 6, 0, 0, NULL),
(1136, 1, 315, 615, 6, 0, 0, NULL),
(1137, 1, 415, 515, 6, 0, 0, NULL),
(1138, 1, 515, 415, 6, 0, 0, NULL),
(1139, 1, 615, 315, 6, 0, 0, NULL),
(1140, 1, 915, 215, 6, 0, 0, NULL),
(1142, 1, 1400, 700, 3, 0, 0, NULL),
(1143, 1, 1500, 700, 3, 0, 0, NULL),
(1144, 1, 1600, 700, 3, 0, 0, NULL),
(1145, 1, 1700, 700, 3, 0, 0, NULL),
(1146, 1, 1800, 700, 3, 0, 0, NULL),
(1147, 1, 1800, 800, 3, 0, 0, NULL),
(1148, 1, 1800, 800, 19, 0, 0, NULL),
(1149, 1, 1800, 900, 19, 0, 0, NULL),
(1150, 1, 1700, 800, 19, 0, 0, NULL),
(1151, 1, 1600, 800, 19, 0, 0, NULL),
(1152, 1, 1700, 900, 19, 0, 0, NULL),
(1153, 1, 1513, 632, 2, 1300, 1800, NULL),
(1154, 1, 2700, 700, 3, 0, 0, NULL),
(1155, 1, 2800, 700, 3, 0, 0, NULL),
(1156, 1, 2600, 700, 3, 0, 0, NULL),
(1157, 1, 2213, 432, 2, 1900, 2600, NULL),
(1158, 1, 2413, 832, 2, 1800, 2600, NULL),
(1160, 1, 2113, 932, 2, 1800, 2600, NULL),
(1161, 1, 2225, 1014, 4, 1800, 2600, NULL),
(1162, 1, 2600, 800, 19, 0, 0, NULL),
(1163, 1, 2600, 900, 19, 0, 0, NULL),
(1164, 1, 2700, 800, 19, 0, 0, NULL),
(1165, 1, 2715, 615, 5, 0, 0, NULL),
(1166, 1, 2900, 700, 3, 0, 0, NULL),
(1167, 1, 3000, 700, 3, 0, 0, NULL),
(1168, 1, 3100, 700, 3, 0, 0, NULL),
(1171, 1, 700, 900, 3, 0, 0, NULL),
(1172, 1, 700, 800, 3, 0, 0, NULL),
(1173, 1, 700, 800, 19, 0, 0, NULL),
(1174, 1, 700, 900, 19, 0, 0, NULL),
(1175, 1, 800, 1000, 3, 0, 0, NULL),
(1176, 1, 900, 1000, 3, 0, 0, NULL),
(1177, 1, 700, 1000, 19, 0, 0, NULL),
(1178, 1, 800, 900, 7, 0, 0, 1179),
(1179, 1, 2100, 0, 7, 0, 0, 1178),
(1180, 1, 2100, 100, 3, 0, 0, NULL),
(1181, 1, 2200, 100, 3, 0, 0, NULL),
(1182, 1, 2215, 15, 9, 0, 0, NULL),
(1183, 1, 3300, 600, 3, 0, 0, NULL),
(1186, 1, 3300, 700, 19, 0, 0, NULL),
(1190, 1, 3300, 800, 19, 0, 0, NULL),
(1192, 1, 3600, 700, 3, 0, 0, NULL),
(1193, 1, 3600, 800, 19, 0, 0, NULL),
(1194, 1, 3600, 900, 19, 0, 0, NULL),
(1195, 1, 3300, 900, 19, 0, 0, NULL),
(1197, 1, 3800, 700, 19, 0, 0, NULL),
(1198, 1, 3800, 800, 19, 0, 0, NULL),
(1199, 1, 3800, 900, 19, 0, 0, NULL),
(1201, 1, 3300, 1000, 19, 0, 0, NULL),
(1202, 1, 3600, 1000, 19, 0, 0, NULL),
(1203, 1, 3800, 1000, 19, 0, 0, NULL),
(1204, 1, 2600, 1000, 19, 0, 0, NULL),
(1205, 1, 1800, 1000, 19, 0, 0, NULL),
(1206, 1, 2915, 615, 6, 0, 0, NULL),
(1207, 1, 3015, 615, 6, 0, 0, NULL),
(1208, 1, 3115, 615, 6, 0, 0, NULL),
(1209, 1, 3315, 515, 6, 0, 0, NULL),
(1210, 1, 3615, 615, 6, 0, 0, NULL),
(1213, 1, 4503, 516, 12, 4300, 4700, NULL),
(1215, 1, 3800, 600, 3, 0, 0, NULL),
(1216, 1, 4300, 600, 3, 0, 0, NULL),
(1217, 1, 4400, 600, 3, 0, 0, NULL),
(1218, 1, 4600, 600, 3, 0, 0, NULL),
(1219, 1, 4500, 600, 3, 0, 0, NULL),
(1220, 1, 4700, 600, 3, 0, 0, NULL),
(1221, 1, 4700, 500, 3, 0, 0, NULL),
(1222, 1, 4700, 600, 19, 0, 0, NULL),
(1223, 1, 4000, 800, 19, 0, 0, NULL),
(1224, 1, 4000, 900, 19, 0, 0, NULL),
(1225, 1, 4000, 1000, 19, 0, 0, NULL),
(1226, 1, 4000, 700, 3, 0, 0, NULL),
(1228, 1, 4300, 700, 19, 0, 0, NULL),
(1229, 1, 4400, 700, 19, 0, 0, NULL),
(1230, 1, 4900, 500, 3, 0, 0, NULL),
(1231, 1, 4800, 500, 3, 0, 0, NULL),
(1232, 1, 5000, 500, 3, 0, 0, NULL),
(1233, 1, 5100, 500, 3, 0, 0, NULL),
(1234, 1, 5200, 500, 3, 0, 0, NULL),
(1235, 1, 5300, 500, 3, 0, 0, NULL),
(1236, 1, 5400, 500, 20, 0, 0, NULL),
(1237, 1, 5500, 500, 20, 0, 0, NULL),
(1238, 1, 5400, 500, 20, 0, 0, NULL),
(1239, 1, 5500, 400, 20, 0, 0, NULL),
(1240, 1, 5500, 300, 20, 0, 0, NULL),
(1241, 1, 5500, 200, 20, 0, 0, NULL),
(1242, 1, 5500, 100, 20, 0, 0, NULL),
(1243, 1, 5500, 0, 20, 0, 0, NULL),
(1244, 1, 5400, 0, 20, 0, 0, NULL),
(1245, 1, 5400, 100, 20, 0, 0, NULL),
(1246, 1, 5400, 300, 20, 0, 0, NULL),
(1247, 1, 5400, 200, 20, 0, 0, NULL),
(1248, 1, 5600, 100, 20, 0, 0, NULL),
(1249, 1, 5600, 0, 20, 0, 0, NULL),
(1250, 1, 5700, 0, 20, 0, 0, NULL),
(1251, 1, 5700, 100, 20, 0, 0, NULL),
(1252, 1, 5600, 200, 20, 0, 0, NULL),
(1253, 1, 5700, 200, 20, 0, 0, NULL),
(1254, 1, 5700, 300, 20, 0, 0, NULL),
(1255, 1, 5800, 300, 20, 0, 0, NULL),
(1256, 1, 5800, 200, 20, 0, 0, NULL),
(1257, 1, 5800, 100, 20, 0, 0, NULL),
(1258, 1, 5800, 0, 20, 0, 0, NULL),
(1259, 1, 5900, 0, 20, 0, 0, NULL),
(1260, 1, 5900, 100, 20, 0, 0, NULL),
(1261, 1, 5900, 200, 20, 0, 0, NULL),
(1262, 1, 5900, 300, 20, 0, 0, NULL),
(1263, 1, 5900, 400, 20, 0, 0, NULL),
(1264, 1, 5900, 500, 20, 0, 0, NULL),
(1265, 1, 5800, 500, 20, 0, 0, NULL),
(1266, 1, 5700, 500, 20, 0, 0, NULL),
(1267, 1, 5600, 500, 20, 0, 0, NULL),
(1268, 1, 5600, 400, 20, 0, 0, NULL),
(1269, 1, 5600, 300, 20, 0, 0, NULL),
(1270, 1, 5700, 400, 20, 0, 0, NULL),
(1271, 1, 5800, 400, 20, 0, 0, NULL),
(1272, 1, 5400, 600, 20, 0, 0, NULL),
(1273, 1, 5500, 600, 20, 0, 0, NULL),
(1274, 1, 5600, 600, 20, 0, 0, NULL),
(1275, 1, 5700, 600, 20, 0, 0, NULL),
(1276, 1, 5800, 600, 20, 0, 0, NULL),
(1277, 1, 5900, 600, 20, 0, 0, NULL),
(1278, 1, 4900, 600, 19, 0, 0, NULL),
(1279, 1, 4800, 600, 19, 0, 0, NULL),
(1280, 1, 5000, 600, 19, 0, 0, NULL),
(1281, 1, 5100, 600, 19, 0, 0, NULL),
(1282, 1, 5200, 600, 19, 0, 0, NULL),
(1283, 1, 5300, 600, 19, 0, 0, NULL),
(1284, 1, 5300, 700, 19, 0, 0, NULL),
(1285, 1, 5200, 700, 19, 0, 0, NULL),
(1286, 1, 5100, 700, 19, 0, 0, NULL),
(1287, 1, 5000, 700, 19, 0, 0, NULL),
(1288, 1, 4900, 700, 19, 0, 0, NULL),
(1289, 1, 4800, 700, 19, 0, 0, NULL),
(1290, 1, 4700, 700, 19, 0, 0, NULL),
(1291, 1, 4600, 700, 19, 0, 0, NULL),
(1292, 1, 4500, 700, 19, 0, 0, NULL),
(1293, 1, 0, 800, 19, 0, 0, NULL),
(1294, 1, 100, 800, 19, 0, 0, NULL),
(1295, 1, 200, 800, 19, 0, 0, NULL),
(1296, 1, 300, 800, 19, 0, 0, NULL),
(1297, 1, 400, 800, 19, 0, 0, NULL),
(1298, 1, 500, 800, 19, 0, 0, NULL),
(1299, 1, 600, 800, 19, 0, 0, NULL),
(1300, 1, 600, 900, 19, 0, 0, NULL),
(1301, 1, 500, 900, 19, 0, 0, NULL),
(1302, 1, 400, 900, 19, 0, 0, NULL),
(1303, 1, 300, 900, 19, 0, 0, NULL),
(1304, 1, 200, 900, 19, 0, 0, NULL),
(1305, 1, 100, 900, 19, 0, 0, NULL),
(1306, 1, 0, 900, 19, 0, 0, NULL),
(1307, 1, 5400, 700, 20, 0, 0, NULL),
(1308, 1, 5500, 700, 20, 0, 0, NULL),
(1309, 1, 5600, 700, 20, 0, 0, NULL),
(1310, 1, 5700, 700, 20, 0, 0, NULL),
(1311, 1, 5800, 700, 20, 0, 0, NULL),
(1312, 1, 5900, 700, 20, 0, 0, NULL),
(1313, 1, 3800, 600, 3, 0, 0, NULL),
(1314, 1, 3815, 515, 6, 0, 0, NULL),
(1315, 1, 4015, 615, 6, 0, 0, NULL),
(1316, 1, 2700, 900, 19, 0, 0, NULL),
(1317, 1, 2800, 800, 19, 0, 0, NULL),
(1318, 1, 4815, 415, 6, 0, 0, NULL),
(1319, 1, 4915, 415, 6, 0, 0, NULL),
(1320, 1, 5015, 415, 6, 0, 0, NULL),
(1321, 1, 5115, 415, 6, 0, 0, NULL),
(1322, 1, 5215, 415, 6, 0, 0, NULL),
(1323, 1, 4315, 515, 6, 0, 0, NULL),
(1324, 1, 4415, 515, 6, 0, 0, NULL),
(1326, 1, 2900, 800, 19, 0, 0, NULL),
(1327, 1, 3000, 800, 19, 0, 0, NULL),
(1328, 1, 3100, 800, 19, 0, 0, NULL),
(1332, 1, 3713, 432, 2, 3300, 4100, NULL),
(1333, 1, 600, 1000, 19, 0, 0, NULL),
(1334, 1, 500, 1000, 19, 0, 0, NULL),
(1335, 1, 400, 1000, 19, 0, 0, NULL),
(1336, 1, 300, 1000, 19, 0, 0, NULL),
(1337, 1, 200, 1000, 19, 0, 0, NULL),
(1338, 1, 100, 1000, 19, 0, 0, NULL),
(1339, 1, 0, 1000, 19, 0, 0, NULL),
(1340, 1, 2315, 15, 6, 0, 0, NULL),
(1341, 1, 2415, 115, 6, 0, 0, NULL),
(1342, 11, 100, 200, 20, 0, 0, NULL),
(1343, 11, 200, 200, 20, 0, 0, NULL),
(1344, 11, 300, 200, 20, 0, 0, NULL),
(1345, 11, 400, 200, 20, 0, 0, NULL),
(1346, 11, 500, 300, 20, 0, 0, NULL),
(1347, 11, 600, 400, 20, 0, 0, NULL),
(1348, 11, 700, 500, 20, 0, 0, NULL),
(1349, 11, 100, 300, 20, 0, 0, NULL),
(1350, 11, 200, 300, 20, 0, 0, NULL),
(1351, 11, 400, 300, 20, 0, 0, NULL),
(1352, 11, 300, 300, 20, 0, 0, NULL),
(1353, 11, 400, 400, 20, 0, 0, NULL),
(1354, 11, 500, 400, 20, 0, 0, NULL),
(1355, 11, 500, 500, 20, 0, 0, NULL),
(1356, 11, 400, 500, 20, 0, 0, NULL),
(1357, 11, 600, 500, 20, 0, 0, NULL),
(1358, 11, 300, 400, 20, 0, 0, NULL),
(1359, 11, 200, 400, 20, 0, 0, NULL),
(1360, 11, 100, 400, 20, 0, 0, NULL),
(1361, 11, 100, 500, 20, 0, 0, NULL),
(1362, 11, 200, 500, 20, 0, 0, NULL),
(1363, 11, 300, 500, 20, 0, 0, NULL),
(1366, 11, 1415, 2515, 21, 0, 0, NULL),
(1367, 11, 1415, 2715, 21, 0, 0, NULL),
(1368, 1, 4300, 800, 19, 0, 0, NULL),
(1369, 1, 4400, 800, 19, 0, 0, NULL),
(1370, 1, 4500, 800, 19, 0, 0, NULL),
(1371, 1, 4600, 800, 19, 0, 0, NULL),
(1372, 1, 4800, 800, 19, 0, 0, NULL),
(1373, 1, 4700, 800, 19, 0, 0, NULL),
(1374, 1, 4900, 800, 19, 0, 0, NULL),
(1375, 1, 5000, 800, 19, 0, 0, NULL),
(1376, 1, 5100, 800, 19, 0, 0, NULL),
(1377, 1, 5300, 800, 19, 0, 0, NULL),
(1378, 1, 5200, 800, 19, 0, 0, NULL),
(1379, 1, 5200, 900, 19, 0, 0, NULL),
(1380, 1, 5300, 900, 19, 0, 0, NULL),
(1381, 1, 5300, 1000, 19, 0, 0, NULL),
(1382, 1, 5200, 1000, 19, 0, 0, NULL),
(1383, 1, 5100, 1000, 19, 0, 0, NULL),
(1384, 1, 5100, 900, 19, 0, 0, NULL),
(1385, 1, 5000, 900, 19, 0, 0, NULL),
(1386, 1, 4900, 1000, 19, 0, 0, NULL),
(1387, 1, 4900, 900, 19, 0, 0, NULL),
(1388, 1, 5000, 1000, 19, 0, 0, NULL),
(1389, 1, 4800, 1000, 19, 0, 0, NULL),
(1390, 1, 4800, 900, 19, 0, 0, NULL),
(1391, 1, 4700, 900, 19, 0, 0, NULL),
(1392, 1, 4400, 900, 19, 0, 0, NULL),
(1393, 1, 4500, 1000, 19, 0, 0, NULL),
(1394, 1, 4500, 900, 19, 0, 0, NULL),
(1395, 1, 4600, 900, 19, 0, 0, NULL),
(1396, 1, 4600, 1000, 19, 0, 0, NULL),
(1397, 1, 4700, 1000, 19, 0, 0, NULL),
(1398, 1, 4300, 1000, 19, 0, 0, NULL),
(1399, 1, 4400, 1000, 19, 0, 0, NULL),
(1400, 1, 4300, 900, 19, 0, 0, NULL),
(1401, 1, 5400, 800, 20, 0, 0, NULL),
(1402, 1, 5500, 800, 20, 0, 0, NULL),
(1403, 1, 5600, 800, 20, 0, 0, NULL),
(1404, 1, 5700, 800, 20, 0, 0, NULL),
(1405, 1, 5900, 800, 20, 0, 0, NULL),
(1406, 1, 5800, 800, 20, 0, 0, NULL),
(1407, 1, 5500, 900, 20, 0, 0, NULL),
(1408, 1, 5400, 900, 20, 0, 0, NULL),
(1409, 1, 5400, 1000, 20, 0, 0, NULL),
(1410, 1, 5500, 1000, 20, 0, 0, NULL),
(1411, 1, 5600, 900, 20, 0, 0, NULL),
(1412, 1, 5600, 1000, 20, 0, 0, NULL),
(1413, 1, 5700, 1000, 20, 0, 0, NULL),
(1414, 1, 5700, 900, 20, 0, 0, NULL),
(1415, 1, 5800, 900, 20, 0, 0, NULL),
(1416, 1, 5800, 1000, 20, 0, 0, NULL),
(1417, 1, 5900, 1000, 20, 0, 0, NULL),
(1418, 1, 5900, 900, 20, 0, 0, NULL),
(1428, 1, 1700, 1000, 19, 0, 0, NULL),
(1431, 1, 1300, 700, 3, 0, 0, NULL),
(1432, 1, 1000, 1000, 3, 0, 0, NULL),
(1433, 1, 1600, 900, 19, 0, 0, NULL),
(1434, 1, 1600, 1000, 19, 0, 0, NULL),
(1435, 1, 1500, 800, 19, 0, 0, NULL),
(1436, 1, 1400, 800, 19, 0, 0, NULL),
(1437, 1, 1500, 900, 19, 0, 0, NULL),
(1438, 11, 800, 500, 20, 0, 0, NULL),
(1439, 11, 700, 400, 20, 0, 0, NULL),
(1440, 11, 600, 300, 20, 0, 0, NULL),
(1441, 11, 500, 200, 20, 0, 0, NULL),
(1443, 1, 15, 15, 25, 0, 0, NULL);

CREATE TABLE `gioco` (
  `nome` varchar(255) NOT NULL,
  `framerate` int(11) NOT NULL,
  `player` varchar(255) DEFAULT NULL,
  `gravita` int(11) NOT NULL DEFAULT '2'
) ;

INSERT INTO `gioco` (`nome`, `framerate`, `player`, `gravita`) VALUES
('Grotte oscure', 60, 'p1', 2);

CREATE TABLE `mappa` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `gioco` varchar(255) NOT NULL,
  `posizione` int(11) DEFAULT NULL,
  `dimCella` int(11) NOT NULL DEFAULT '100',
  `numCelleAsse` int(11) NOT NULL DEFAULT '10',
  `cellaInizioX` int(11) NOT NULL DEFAULT '1',
  `cellaInizioY` int(11) NOT NULL DEFAULT '1',
  `cellaFineX` int(11) NOT NULL DEFAULT '5',
  `cellaFineY` int(11) NOT NULL DEFAULT '5',
  `immagineCellaFine` varchar(255) DEFAULT NULL,
  `immagineCellaInizio` varchar(255) DEFAULT NULL
) ;

INSERT INTO `mappa` (`id`, `nome`, `gioco`, `posizione`, `dimCella`, `numCelleAsse`, `cellaInizioX`, `cellaInizioY`, `cellaFineX`, `cellaFineY`, `immagineCellaFine`, `immagineCellaInizio`) VALUES
(1, 'Entrata', 'Grotte oscure', 1, 100, 60, 1, 1, 54, 4, 'porta', NULL),
(11, 'Castello Alfa', 'Grotte oscure', 2, 100, 40, 1, 1, 30, 9, 'porta', 'porta');

CREATE TABLE `oggetto_droppato` (
  `sprite_id` int(11) NOT NULL,
  `droppato_id` int(11) NOT NULL
) ;

INSERT INTO `oggetto_droppato` (`sprite_id`, `droppato_id`) VALUES
(2, 6),
(4, 9);

CREATE TABLE `salvataggio` (
  `gioco` varchar(255) NOT NULL,
  `num` int(11) NOT NULL,
  `mappa` int(11) NOT NULL,
  `punteggio` int(11) DEFAULT '0'
) ;

CREATE TABLE `sprite` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `salute` int(11) DEFAULT '0',
  `riposo` int(11) DEFAULT '0',
  `velocita` int(11) DEFAULT '0',
  `attacco` int(11) DEFAULT '0',
  `valore` int(11) DEFAULT '0',
  `ripososparo` int(11) DEFAULT '-1',
  `testo` varchar(500) DEFAULT NULL,
  `punteggio_fornito` int(11) DEFAULT '0',
  `proiettile` varchar(255) DEFAULT NULL,
  `piattaforma` tinyint(1) DEFAULT '0'
) ;

INSERT INTO `sprite` (`id`, `nome`, `tipo`, `salute`, `riposo`, `velocita`, `attacco`, `valore`, `ripososparo`, `testo`, `punteggio_fornito`, `proiettile`, `piattaforma`) VALUES
(1, 'p1', 'player', 100, 100, 5, 200, 0, 30, '', 0, 'fuoco', 0),
(2, 'fly', 'npc', 100, 0, 2, 40, 0, -1, NULL, 10, NULL, 0),
(3, 'grass', 'block', 0, 0, 0, 0, 0, -1, NULL, 50, NULL, 0),
(4, 'ghost', 'npc', 500, 0, 2, 80, 0, -1, NULL, 50, NULL, 0),
(5, 'cherry', 'health', 0, 0, 0, 0, 120, -1, NULL, 0, NULL, 0),
(6, 'stelle', 'collectable', 0, 0, 0, 0, 5, -1, NULL, 5, NULL, 0),
(7, 'porta', 'portal', 0, 0, 0, 0, 1, -1, NULL, 0, NULL, 0),
(9, 'bomb', 'bullets', 0, 0, 0, 0, 10, -1, NULL, 0, NULL, 0),
(11, 'bridge', 'block', 0, 0, 0, 0, 0, -1, NULL, 0, NULL, 1),
(12, 'demone', 'npc', 80, 0, 1, 10, 0, 100, '', 20, 'fuoco', 0),
(17, 'proiettile', 'bullet', 0, 0, 10, 100, 0, -1, NULL, 0, NULL, 0),
(18, 'fuoco', 'bullet', 0, 0, 8, 50, 0, -1, NULL, 0, NULL, 0),
(19, 'grassCenter', 'block', 0, 0, 5, 0, 0, -1, NULL, 0, NULL, 0),
(20, 'castle', 'block', 0, 0, 5, 0, 0, -1, NULL, 0, NULL, 0),
(21, 'gemme', 'collectable', 0, 0, 0, 0, 1, -1, NULL, 30, NULL, 0);


ALTER TABLE `collezionabile`
  ADD PRIMARY KEY (`id`),
  ADD KEY `collezionabile_gioco_numsalvataggio_index` (`gioco`,`numsalvataggio`);

ALTER TABLE `collocazione`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mappa_id` (`mappa_id`),
  ADD KEY `Collocazione_sprite_id_uindex` (`sprite_id`);

ALTER TABLE `gioco`
  ADD PRIMARY KEY (`nome`),
  ADD KEY `gioco_sprite_id_fk` (`player`);

ALTER TABLE `mappa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mappa_gioco_nome_fk` (`gioco`),
  ADD KEY `Index` (`nome`,`gioco`);

ALTER TABLE `oggetto_droppato`
  ADD PRIMARY KEY (`sprite_id`,`droppato_id`),
  ADD KEY `droppato_id_fk` (`droppato_id`);

ALTER TABLE `salvataggio`
  ADD PRIMARY KEY (`gioco`,`num`),
  ADD KEY `mappa` (`mappa`);

ALTER TABLE `sprite`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sprite_nome_uindex` (`nome`);


ALTER TABLE `collezionabile`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `collocazione`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `mappa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `sprite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `collezionabile`
  ADD CONSTRAINT `collezionabile_salvataggio_gioco_num_fk` FOREIGN KEY (`gioco`,`numsalvataggio`) REFERENCES `salvataggio` (`gioco`, `num`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `collocazione`
  ADD CONSTRAINT `collocazione_ibfk_1` FOREIGN KEY (`mappa_id`) REFERENCES `mappa` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
