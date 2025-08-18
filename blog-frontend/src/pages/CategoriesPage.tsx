import { apiService, Category } from "@/service/apiService";
import { Button, Card, CardBody, CardHeader, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, TableBody, TableCell, TableColumn, TableHeader, TableRow, Tooltip, useDisclosure, Table } from "@nextui-org/react";
import { Edit2, Plus, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";



interface CategoriesPageProps {
    isAuthenticated: boolean;
}
const CategoriesPage: React.FC<CategoriesPageProps> = ({
    isAuthenticated
}) => {
    const [categories, setCategories] = useState<Category[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [editingCategory, setEditingCategory] = useState<Category | null>(null);
    const [newCategoryName, setNewCategoryName] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { isOpen, onOpen, onClose } = useDisclosure();

    useEffect(()=> {
        fetchCategories();
    }, [])

    const fetchCategories = async () => {
        try {
            setLoading(true);
            const response = await apiService.getCategories();
            setCategories(response);
            setError(null)
        } catch (err) {
            setError("Failed to load categories. Please try again later.");
        } finally {
            setLoading(false);
        }
    };

    const handleAddEdit = async () => {
        if (!newCategoryName.trim()) {
            return;
        }

        try {
            setIsSubmitting(true);
            if (editingCategory) {
                await apiService.updateCategory(
                    editingCategory.uuid,
                    newCategoryName.trim()
                );
            } else {
                await apiService.createCategory(newCategoryName.trim());
            }
            await fetchCategories();
            handleModalClose();
        } catch (err) {
            setError(
                `Failed to ${editingCategory ? "update" : "create"} category. Please try again.`
            );
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (category: Category) => {
        if (
            !window.confirm(
                `Are you sure want to delete the category "${category.name}"`
            )
        ) {
            return;
        }

        try {
            setLoading(true);
            await apiService.deleteCategory(category.uuid);
            await fetchCategories();
        }
        catch (err) {
            setError("Failed to delete category. Please try again");
        } finally {
            setLoading(false);
        }
    };

    const handleModalClose = () => {
        setEditingCategory(null);
        setNewCategoryName("");
        onClose();
    };

    const openEditModal = (category: Category) => {
        setEditingCategory(category);
        setNewCategoryName(category.name);
        onOpen();
    };

    const openAddModal = () => {
        setEditingCategory(null);
        setNewCategoryName("");
        onOpen();
    };

    return (
        <div className="max-w-4xl mx-auto px-4">
            <Card>
                <CardHeader className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold">
                        Categories
                    </h1>
                    {isAuthenticated && (
                        <Button
                            color="primary"
                            startContent={<Plus size={16}/>}
                            onPress={openAddModal}
                        >   
                            Add Category
                        </Button>
                    )}
                </CardHeader>
                <CardBody>
                    {error && (
                        <div className="mb-4 p-4 text-red-500 bg-red-50 rounded-lg">
                            {error}
                        </div>
                    )}

                    <Table
                    aria-label="Categories table"
                    isHeaderSticky
                    classNames={{
                        wrapper: "max-h-[600px]"
                    }}
                    >
                        <TableHeader>
                            <TableColumn>NAME</TableColumn>
                            <TableColumn>POST COUNT</TableColumn>
                            <TableColumn>ACTIONS</TableColumn>
                        </TableHeader>
                        <TableBody
                        isLoading={loading}
                        loadingContent={<div>Loading categories...</div>}
                        >
                            {categories.map((category) => (
                                <TableRow key={category.uuid}>
                                    <TableCell>{category.name}</TableCell>
                                    <TableCell>{category.postCount || 0}</TableCell>
                                    <TableCell>
                                        {isAuthenticated ? (
                                            <div className="flex gap-2">
                                                <Button
                                                isIconOnly
                                                variant="flat"
                                                size="sm"
                                                onPress={() => openEditModal(category)}
                                                >
                                                    <Edit2 size={16}/>     
                                                </Button>
                                                <Tooltip
                                                content={
                                                    category.postCount ? "Cannot delete category with exiting posts" : "Delete category"
                                                }
                                                >
                                                   <Button
                                                   isIconOnly
                                                   variant="flat"
                                                   color="danger"
                                                   size="sm"
                                                   onPress={() => handleDelete(category)}
                                                   isDisabled={
                                                    category?.postCount ? category.postCount > 0 : false
                                                   }
                                                   >
                                                    <Trash2 size={16} />
                                                   </Button>
                                                </Tooltip>
                                            </div>
                                        ): (
                                            <span>-</span>
                                        )}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </CardBody>
            </Card>
            <Modal isOpen={isOpen} onClose={handleModalClose}>
                <ModalContent>
                    <ModalHeader>
                        {editingCategory ? "Edit Category"
                         : "Add Category" }
                    </ModalHeader>
                    <ModalBody>
                        <Input
                        label="Category Name"
                        value={newCategoryName}
                        onChange={(e) => setNewCategoryName(e.target.value)}
                        isRequired
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button variant="flat" onPress={handleModalClose}>
                            Cancel
                        </Button>
                        <Button
                        color="primary"
                        onPress={handleAddEdit}
                        isLoading={isSubmitting}
                        >
                            {editingCategory ? "Update" : "Add"}
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </div>
    );
};

export default CategoriesPage;