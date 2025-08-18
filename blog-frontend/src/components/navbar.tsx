import React from "react";
import { Link, useLocation } from "react-router-dom";
import {BookDashed, Edit3, LogOut, Plus} from "lucide-react";
import { Navbar, 
  NavbarContent,
   NavbarMenuToggle,
   NavbarBrand,
   NavbarItem,
   Button,
   Dropdown,
   Avatar,
   DropdownTrigger,
   DropdownMenu,
   DropdownItem,
   NavbarMenu,
   NavbarMenuItem
  } from '@nextui-org/react';


interface NavBarProps {
  isAuthenticated: boolean;
  userProfile?: {
    name: string;
    avatar?: string;
  };
  onLogout: () => void;
}

const NavBar: React.FC<NavBarProps> = ({
  isAuthenticated,
  userProfile,
  onLogout,
}) => {
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const menuItems = [
    { name: 'Home', path: '/' },
    { name: 'Categories', path: '/categories' },
    { name: 'Tags', path: '/tags' }
  ];

  return (
    <Navbar 
    isBordered
    className="mb-6"
    isMenuOpen={isMenuOpen}
    onMenuOpenChange={setIsMenuOpen}
    >
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>
  
      <NavbarContent className="sm:hidden pr-3" justify="center">
        <NavbarBrand>
          <Link className="font-bold text-inherit" to="/">Blog Platform</Link>
        </NavbarBrand>
      </NavbarContent>
  
      <NavbarContent className="hidden sm:flex gap-4" justify="start">
        <NavbarBrand>
          <Link className="font-bold text-inherit" to="/">
            Blog Platform
          </Link>
        </NavbarBrand>
        {menuItems.map((item) => (
          <NavbarItem 
          key={item.path}
          isActive={location.pathname === item.path}
          >
            <Link
            className={`text-sm ${location.pathname === item.path ? 'text-primary' : 'text-default-600'}`} to={item.path}
            >
              {item.name}
            </Link>
          </NavbarItem>
        ))}
      </NavbarContent>
      
      <NavbarContent justify="end">
        {isAuthenticated ? (
          <>
            <NavbarItem>
              <Button
              as={Link}
              color="secondary"
              startContent={<BookDashed size={16} />}
              to="/posts/drafts"
              variant="flat"
              >
                Draft Post
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Button
              as={Link}
              color="primary"
              startContent={<Plus size={16} />}
              to="/posts/new"
              variant="flat"
              >
                New Post
              </Button>
            </NavbarItem>

            <NavbarItem>
              <Dropdown placement="bottom-end">
                <DropdownTrigger>
                  <Avatar
                  isBordered
                  as="button"
                  className="transition-transform"
                  name={userProfile?.name}
                  src={userProfile?.avatar}
                  />        
                </DropdownTrigger>
                <DropdownMenu aria-label="User menu">
                  <DropdownItem
                    key="drafts"
                    startContent={<Edit3 size={16}/>}
                  >
                    <Link to="/posts/drafts"> My Drafts</Link>
                  </DropdownItem>
                  <DropdownItem
                    key="logout"
                    className="text-danger"
                    color="danger"
                    startContent={<LogOut size={16} />}
                    onPress={onLogout}
                  >
                    Log Out
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </NavbarItem>
          </>
        ) : (<>
            <NavbarItem>
              <Button as={Link} to="/login" variant="flat">
                Log In
              </Button>
            </NavbarItem>
        </>)}
      </NavbarContent>

      <NavbarMenu>
        {menuItems.map((item) => (
          <NavbarMenuItem key={item.path}>
            <Link
            className={`w-full ${
              location.pathname === item.path ? 'text-primary' : 'text-default-600'}`}
            to={item.path}
            onClick={() => setIsMenuOpen(false)}
            >
              {item.name}
            </Link>
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
    </Navbar>
  );
};


export default NavBar