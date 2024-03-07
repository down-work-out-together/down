"use client";

import Link from "next/link";
import Navigation from "./Navigation";
import { LogOut } from "lucide-react";

import { useProfile } from "@/hooks/user/useProfile";
import { useLogout } from "@/app/(auth)/hooks/login/useLogout";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui";

const Header = () => {
  const user = useProfile();
  const logout = useLogout();
  const router = useRouter();

  return (
    <header className="sticky top-0 z-50 w-screen h-14 flex items-center bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex justify-between items-center">
        <Link href="/">
          <button className="font-semibold text-xl">로고</button>
        </Link>
        <Navigation />
        {user ? (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline">{user.nickName} 님</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuLabel>계정 관리</DropdownMenuLabel>

              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={() => router.push("/mypage")}>
                마이 페이지
              </DropdownMenuItem>

              <DropdownMenuSeparator />
              <DropdownMenuItem
                onClick={() => logout()}
                className="justify-between items-center"
              >
                로그아웃 <LogOut className="w-4 h-4 text-stone-500" />
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        ) : (
          <Link href="/login">
            <Button variant="outline">로그인</Button>
          </Link>
        )}
      </div>
    </header>
  );
};

export default Header;
