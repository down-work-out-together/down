import Link from "next/link";

const navLinks = [
	{ label: "모임", href: "/meetups" },
	{ label: "강습", href: "/lesson" },
	{ label: "중고거래", href: "/fleamarket" },
	{ label: "채팅", href: "/chat" },
];

const NavLinks = () => {
	return (
		<nav>
			<ul className="flex gap-6 items-center">
				{navLinks.map((link) => (
					<li key={link.label}>
						<Link href={link.href}>{link.label}</Link>
					</li>
				))}
			</ul>
		</nav>
	);
};

export default NavLinks;
